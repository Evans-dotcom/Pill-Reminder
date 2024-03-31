package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_MESSAGES;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageRepository {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private MessageDAO messageDAO;
    private LiveData<List<Message>> groupedMessages;
    private CollectionReference messagesCollection;
    private CollectionReference individualMessagesCollection;
    private FirebaseFirestore firestore;
    private volatile boolean updating = false;

    public MessageRepository(Application application) {
        MDatabase messageDB = MDatabase.getInstance(application);
        messageDAO = messageDB.messageDAO();
        groupedMessages = messageDAO.getGroupedMessages();
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public void insert(Message message) {
        message.setSynced(false);

        new InsertAsyncTask(messageDAO).execute(message);

        if (!isUpdating()) {
            uploadLocalChangesToFirestore();
        }
    }

    public void update(Message message) {
        message.setSynced(false);

        new UpdateAsyncTask(messageDAO).execute(message);

        if (!isUpdating()) {
            uploadLocalChangesToFirestore();
        }
    }

    private void uploadLocalChangesToFirestore() {
        setUpdating(true);
        MessageRepository.UploadUnsyncData.OnTaskCompleteListener<List<Message>> listener = result -> Log.i(MY_TAG, "onTaskComplete: " + result.get(0));
        new MessageRepository.UploadUnsyncData(messageDAO, getFirestore(), listener).execute();
    }

    public LiveData<List<Message>> getGroupedMessages() {
        try {
            return new GetGroupedMessages(messageDAO).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> getIndividualsMessage(String userID, String recipientID) {
        messageDAO.getMessagesByReceiverID(userID, recipientID);
        try {
            return new GetIndividualsMessages(messageDAO, userID, recipientID).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> getGroupedMessagesList() {
//        messageDAO.getMessagesByReceiverID();
        try {
            return new GetGroupedMessagesList(messageDAO).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CollectionReference getIndividualMessagesCollection() {
        // FIXME: later
        if (individualMessagesCollection == null) {
            individualMessagesCollection = getFirestore()
                    .collection(DB_FIRESTORE_COLLECTIONS_MESSAGES);
        }
        return individualMessagesCollection;
    }

    public CollectionReference getMessagesCollection() {
        if (messagesCollection == null) {
            messagesCollection = getFirestore().collection(DB_FIRESTORE_COLLECTIONS_MESSAGES);
        }
        return messagesCollection;
    }

    public FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    private static class InsertAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDAO messageDAO;

        public InsertAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDAO.insert(messages[0]);
            Log.w(MY_TAG, "doInBackground: " + messages[0].getMessage());
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDAO messageDao;

        public UpdateAsyncTask(MessageDAO messageDAO) {
            this.messageDao = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.update(messages[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDAO messageDAO;

        public DeleteAsyncTask(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDAO.delete(messages[0]);
            return null;
        }
    }

    private static class UploadUnsyncData extends AsyncTask<Void, Void, List<Message>> {
        private final FirebaseFirestore firebaseFirestore;
        private final FirebaseAuth firebaseAuth;
        private final MessageRepository.UploadUnsyncData.OnTaskCompleteListener<List<Message>> listener;
        private MessageDAO messageDAO;
        private CollectionReference messagesCollection;

        public UploadUnsyncData(MessageDAO messageDAO, FirebaseFirestore firebaseFirestore, MessageRepository.UploadUnsyncData.OnTaskCompleteListener<List<Message>> listener) {
            this.messageDAO = messageDAO;
            this.firebaseFirestore = firebaseFirestore;
            this.listener = listener;
            this.firebaseAuth = FirebaseAuth.getInstance();
        }

        @Override
        protected List<Message> doInBackground(Void... voids) {
            List<Message> unsyncedMessages = messageDAO.getUnsyncedMessages();
            Log.i(MY_TAG, "doInBackground: " + unsyncedMessages.size());
            return unsyncedMessages;
        }

        private CollectionReference getMessagesCollection() {
            if (messagesCollection == null) {
                messagesCollection = firebaseFirestore.collection(DB_FIRESTORE_COLLECTIONS_MESSAGES);
            }
            Log.i(MY_TAG, "getMessagesCollection: " + messagesCollection.getPath());
            return messagesCollection;
        }

        public MessageDAO messageDAO() {
            return messageDAO;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
//            super.onPostExecute(messages);
            for (Message message : messages) {
                DocumentReference mDocument = getMessagesCollection().document();
                Log.w(MY_TAG, "success:about upload: " + mDocument.getPath());

                mDocument.set(message.toMap(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()))
                        .addOnSuccessListener(aVoid -> {
                            Log.w(MY_TAG, "success: " + mDocument.getId());
                            // Update sync status in Room database
                            message.setDocumentId(mDocument.getId());
                            message.setSynced(true);
                            databaseWriteExecutor.execute(() -> {
                                messageDAO().update(message);
                                messageDAO.update(message);
                            });
                        });
            }

            Log.i(MY_TAG, "onPostExecute: Local changes uploaded to Firestore");
        }

        public interface OnTaskCompleteListener<T> {
            void onTaskComplete(T result);
        }
    }

    private class GetIndividualsMessages extends AsyncTask<Void, Void, List<Message>> {
        MessageDAO messageDAO;
        String receiverID, userID;

        public GetIndividualsMessages(MessageDAO messageDAO, String userID, String recipientID) {
            this.messageDAO = messageDAO;
            this.receiverID = recipientID;
            this.userID = userID;
        }

        @Override
        protected List<Message> doInBackground(Void... voids) {
            return messageDAO.getMessagesByReceiverID(userID, receiverID);
        }
    }

    private class GetGroupedMessages extends AsyncTask<Void, Void, LiveData<List<Message>>> {
        MessageDAO messageDAO;

        public GetGroupedMessages(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected LiveData<List<Message>> doInBackground(Void... voids) {
            return messageDAO.getGroupedMessages();
        }
    }

    private class GetGroupedMessagesList extends AsyncTask<Void, Void, List<Message>> {
        MessageDAO messageDAO;

        public GetGroupedMessagesList(MessageDAO messageDAO) {
            this.messageDAO = messageDAO;
        }

        @Override
        protected List<Message> doInBackground(Void... voids) {
            return messageDAO.getGroupedMessagesList();
        }
    }
}
