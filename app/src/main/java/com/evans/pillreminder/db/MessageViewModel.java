package com.evans.pillreminder.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private final LiveData<List<Message>> groupedMessages;
    private final MessageRepository messageRepository;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
        groupedMessages = messageRepository.getGroupedMessages();
    }

    public LiveData<List<Message>> getGroupedMessages() {
        return groupedMessages;
    }

    public void insert(Message message) {
        messageRepository.insert(message);
    }

    public void update(Message message) {
        messageRepository.update(message);
    }

    public List<Message> getIndividualsMessages(String receiverID) {
        return messageRepository.getIndividualsMessage(receiverID);
    }
}
