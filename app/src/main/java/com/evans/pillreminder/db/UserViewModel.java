package com.evans.pillreminder.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {
    private final LiveData<User> user;
    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
        user = userRepository.getUser();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void insertFirst(User user) {
        userRepository.insertFirst(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void deleteUserData() {
        userRepository.deleteAny();
    }

    public void deleteAny() {
        userRepository.deleteAny();
    }
}
