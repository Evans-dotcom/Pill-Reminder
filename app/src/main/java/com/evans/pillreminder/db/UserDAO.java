package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_USER_TABLE_NAME;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFirst(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM " + DB_USER_TABLE_NAME)
    LiveData<User> getUser();

    @Query("SELECT * FROM " + DB_USER_TABLE_NAME + " WHERE  'synced' = 0")
    LiveData<User> getUnsyncedUser();

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM " + DB_USER_TABLE_NAME)
    void deleteAny();
}
