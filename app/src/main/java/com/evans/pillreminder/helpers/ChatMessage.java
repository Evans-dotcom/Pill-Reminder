package com.evans.pillreminder.helpers;

public class ChatMessage {
    String message, messageTime;

    public ChatMessage(String message, String messageTime) {
        this.message = message;
        this.messageTime = messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
