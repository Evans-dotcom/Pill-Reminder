package com.evans.pillreminder.helpers;

public class MessageView {
    String senderName, lastMessageTime, lastMessage;

    public MessageView(String senderName, String lastMessageTime, String lastMessage) {
        this.senderName = senderName;
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
