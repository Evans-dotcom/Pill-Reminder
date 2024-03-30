package com.evans.pillreminder;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_USERS;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_FIELD_USER_TOKEN;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.pillreminder.adapters.ChatAdapter;
import com.evans.pillreminder.db.Message;
import com.evans.pillreminder.db.MessageRepository;
import com.evans.pillreminder.helpers.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    EditText etChatInput;
    ImageButton cardChatSend;
    RecyclerView chatFragmentRecyclerView;
    String userID, recipientID, recipientToken;
    MessageRepository messageRepository;
    private FirebaseFirestore firestore;
    private List<Message> messagesByReceiverID;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        recipientID = getIntent().getStringExtra("recipientID");
        recipientToken = getIntent().getStringExtra("recipientToken");

        messageRepository = new MessageRepository(getApplication());


//        adapter = new ChatAdapter(Message.getChatMessagesFormat(messagesByReceiverID));
        adapter = new ChatAdapter();
        new Thread(() -> {
            messagesByReceiverID = messageRepository.getIndividualsMessage(recipientID);
            adapter.setChatMessages(Message.getChatMessagesFormat(messagesByReceiverID));
            Log.d(MY_TAG, ">>: " + Message.getChatMessagesFormat(messagesByReceiverID).size());
            adapter.notifyDataSetChanged();
        }).start();

        firestore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection(Constants.DB_FIRESTORE_COLLECTIONS_USERS).document(recipientID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recipientToken = task.getResult().getString(Constants.DB_FIRESTORE_FIELD_USER_TOKEN);
            } else {
                // recipient not found
                Toast.makeText(this, "Recipient not found", Toast.LENGTH_SHORT).show();
            }
        });

        etChatInput = findViewById(R.id.etChatInput);
        cardChatSend = findViewById(R.id.cardChatSendFragment);
        chatFragmentRecyclerView = findViewById(R.id.chatFragRecyclerView);

//        ChatAdapter adapter = new ChatAdapter(messagesByReceiverID);
        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setReverseLayout(true);
        chatFragmentRecyclerView.setLayoutManager(manager);
        chatFragmentRecyclerView.setAdapter(adapter);
//        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatFragmentRecyclerView.smoothScrollToPosition(0);
            }
        });

        cardChatSend.setOnClickListener(v -> {
            String message = etChatInput.getText().toString();
            if (!message.isEmpty()) {
                Log.d(MY_TAG, "ChatActivity: " + message);
                sendMessageToRecipient(message);
                etChatInput.setText("");
            } else {
                etChatInput.requestFocus();
            }
        });
    }

    private void sendMessageToRecipient(String message) {
        // send message to recipient
        Log.d(MY_TAG, "UID: " + userID + " RID: " + recipientID + " RTk: " + recipientToken + " MSG: " + message);
        CollectionReference mCollection = firestore.collection(Constants.DB_FIRESTORE_COLLECTIONS_MESSAGES)
                .document(userID + "-" + recipientID)
                .collection(Constants.DB_FIRESTORE_SUB_COLLECTIONS_MESSAGE);

        long timestamp = System.currentTimeMillis();

        Map<String, Object> mMessage = Map.of("message", message, "timestamp", timestamp, "senderID", userID);
        Log.d(MY_TAG, "MessageToRecipient: " + mMessage);

        mCollection.document(String.valueOf(timestamp)).set(mMessage).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // message sent, send FCM notification to the recipient
                RemoteMessage messageToSend = new RemoteMessage.Builder(recipientToken)
                        .setMessageId(String.valueOf(timestamp))
                        .addData("message", message)
                        .addData("timestamp", String.valueOf(timestamp))
                        .addData("senderID", userID)
                        .setTtl(86400)
                        .build();

                FirebaseMessaging.getInstance().send(messageToSend);

                Log.w(MY_TAG, "SendMessage: " + messageToSend);
                // save message to local database
                firestore.collection(DB_FIRESTORE_COLLECTIONS_USERS)
                        .document(recipientID)
                        .get()
                        .addOnCompleteListener((aTask) -> {
                            String token = Objects.requireNonNull(aTask.getResult().get(DB_FIRESTORE_FIELD_USER_TOKEN)).toString();
                            messageRepository.insert(new Message(message, recipientID, timestamp, token));

                            new Thread(() -> {
                                messagesByReceiverID = messageRepository.getIndividualsMessage(recipientID);
                                runOnUiThread(() -> {
                                    adapter.setChatMessages(Message.getChatMessagesFormat(messagesByReceiverID));
                                    adapter.notifyDataSetChanged();
                                });
                            }).start();
                        });
            } else {
                // message not sent
                Log.e(MY_TAG, "MessageToRecipient: " + task.getException());
            }
        });
    }
}