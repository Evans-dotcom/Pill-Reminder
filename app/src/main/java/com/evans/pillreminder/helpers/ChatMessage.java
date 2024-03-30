package com.evans.pillreminder.helpers;

public class ChatMessage {
    String message;
    long messageTime;
    String recipientID;

    public ChatMessage(String message, long messageTime, String recipientID) {
        this.message = message;
        this.messageTime = messageTime;
        this.recipientID = recipientID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
