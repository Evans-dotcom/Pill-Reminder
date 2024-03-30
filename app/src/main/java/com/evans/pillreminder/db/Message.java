package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME_MESSAGES;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.evans.pillreminder.helpers.ChatMessage;
import com.evans.pillreminder.helpers.MessageView;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
@Entity(tableName = DB_TABLE_NAME_MESSAGES)
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int messageID;
    private String message;
    private String receiverID;
    private long timestamp;
    private boolean synced;
    private String documentId;
    private String recipientToken;

    public Message() {
    }

    public Message(String message, String receiverID, long timestamp, String receiverToken) {
        this.message = message;
        this.receiverID = receiverID;
        this.timestamp = timestamp;
        this.synced = false;
        this.documentId = "EMPTY";
        this.recipientToken = receiverToken;
    }

    public static List<ChatMessage> getChatMessagesFormat(List<Message> messages) {
        // FIXME: not efficient
        List<ChatMessage> chatMessages = new ArrayList<>();
        if (messages != null) {
            if (!messages.isEmpty()) {
                messages.forEach(message -> chatMessages.add(new ChatMessage(message.getMessage(), message.getTimestamp(), message.getReceiverID())));
            }
        }
        return chatMessages;
    }

    public static List<MessageView> getMessageViewFormat(List<Message> messages) {
        // FIXME: not efficient
        List<MessageView> messageList = new ArrayList<>();
        if (messages != null) {
            if (!messages.isEmpty()) {
                messages.forEach(message -> messageList.add(new MessageView("Test Name Alpha", message.getTimestamp(), message.getMessage(), message.getReceiverID(), message.getRecipientToken())));
            }
        }
        return messageList;
    }

    public String getRecipientToken() {
        return recipientToken;
    }

    public void setRecipientToken(String recipientToken) {
        this.recipientToken = recipientToken;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> toMap(String userID) {
        return Map.of(
                "senderID", userID,
                "message", message,
                "timestamp", timestamp);
    }

    public ChatMessage toChatMessage(String userID) {
        return new ChatMessage(message, timestamp, userID);
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}