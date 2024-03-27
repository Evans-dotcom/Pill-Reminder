package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final LiveData<User> user;

    private UserDAO userDAO;
    private CollectionReference userCollection;
    private FirebaseFirestore firestore;
    private volatile boolean updating = false;

    UserRepository(Application application) {
        MedicationDatabase medDB = MedicationDatabase.getInstance(application);
        userDAO = medDB.userDAO();
        user = userDAO.getUser();
        Log.i(MY_TAG, "DB: " + medDB.isOpen() + " D:" + " U:" + user.getValue());
    }

    public FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    public void insert(User user) {
        user.setSynced(false);
        new InsertAsyncTask(userDAO).execute(user);
        if (!isUpdating()) {
            uploadLocalChangesToFirestore();
        }
    }

    public void insertFirst(User user) {
        new InsertFirstAsyncTask(userDAO).execute(user);
        if (!isUpdating()) {
//            uploadLocalChangesToFirestore();
        }
    }

    public void update(User user) {
        user.setSynced(false);
        new UpdateAsyncTask(userDAO).execute(user);
        if (!isUpdating()) {
            uploadLocalChangesToFirestore();
        }
    }

    public void deleteUser(User user) {
        user.setSynced(false);
        new DeleteAsyncTask(userDAO).execute(user);
        if (!isUpdating()) {
            uploadLocalChangesToFirestore();
        }
    }

    public void deleteAny() {
//        user.setSynced(false);
        new DeleteAnyAsyncTask(userDAO).execute();
//        if (!isUpdating()) {
//            uploadLocalChangesToFirestore();
//        }
    }

    private void uploadLocalChangesToFirestore() {
        setUpdating(true);
        UploadUnsyncData.OnTaskCompleteListener<User> listener = result -> Log.i(MY_TAG, "onTaskComplete: " + result.toString());
        new UploadUnsyncData(userDAO, getFirestore(), listener).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public InsertAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insert(users[0]);
            Log.w(MY_TAG, "Insert doInBackground: " + users[0].getFirstName());
            return null;
        }
    }

    private static class InsertFirstAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public InsertFirstAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insertFirst(users[0]);
            Log.w(MY_TAG, "Insert doInBackground: " + users[0].getFirstName());
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public UpdateAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.update(users[0]);
            Log.w(MY_TAG, "Update doInBackground: " + users[0].getFirstName());
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        public DeleteAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.deleteUser(users[0]);
            Log.w(MY_TAG, "Delete doInBackground: " + users[0].getFirstName());
            return null;
        }
    }

    private static class DeleteAnyAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDAO userDAO;

        public DeleteAnyAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDAO.deleteAny();
            return null;
        }
    }

    public static class UploadUnsyncData extends AsyncTask<Void, Void, User> {
        private final FirebaseFirestore firebaseFirestore;
        private final OnTaskCompleteListener<User> listener;
        private UserDAO userDAO;
        private CollectionReference usersCollection;

        public UploadUnsyncData(UserDAO userDAO, FirebaseFirestore firebaseFirestore, OnTaskCompleteListener<User> listener) {
            this.firebaseFirestore = firebaseFirestore;
            this.userDAO = userDAO;
            this.listener = listener;
        }

        @Override
        protected User doInBackground(Void... voids) {
            User unsyncedUser = userDAO.getUnsyncedUser().getValue();
            return unsyncedUser;
        }

        private CollectionReference getUsersCollection() {
            if (usersCollection == null) {
                usersCollection = firebaseFirestore.collection(DB_FIRESTORE_COLLECTIONS_USERS);
                ;
            }
            return usersCollection;
        }

        public UserDAO userDAO() {
            return userDAO;
        }

        @Override
        protected void onPostExecute(User user) {
//            super.onPostExecute(user);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            DocumentReference mDocument = getUsersCollection().document(uid);

            mDocument.set(user.toMap())
                    .addOnSuccessListener(aVoid -> {
                        Log.w(MY_TAG, "Success: " + mDocument.getId());
                        user.setSynced(true);
                        databaseWriteExecutor.execute(() -> {
                            userDAO().update(user);
                            userDAO.update(user);
                        });
                    });
            Log.i(MY_TAG, "Local changes updated successfully");
        }

        public interface OnTaskCompleteListener<T> {
            void onTaskComplete(T result);
        }

    }
}
