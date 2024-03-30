package com.evans.pillreminder.helpers;

public class MessageView {
    String senderName, lastMessage, recipientID, recipientToken;
    long lastMessageTime;

    public MessageView(String senderName, long lastMessageTime, String lastMessage, String recipientID, String recipientToken) {
        this.senderName = senderName;
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
        this.recipientID = recipientID;
        this.recipientToken = recipientToken;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getRecipientToken() {
        return recipientToken;
    }

    public void setRecipientToken(String recipientToken) {
        this.recipientToken = recipientToken;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
