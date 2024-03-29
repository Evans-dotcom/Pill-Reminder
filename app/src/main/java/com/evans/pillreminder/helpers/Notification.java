package com.evans.pillreminder.helpers;

public class Notification {
    String title, message, sentTimestamp, senderID;

    public Notification(String title, String message, String sentTimestamp, String senderID) {
        this.title = title;
        this.message = message;
        this.sentTimestamp = sentTimestamp;
        this.senderID = senderID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(String sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
