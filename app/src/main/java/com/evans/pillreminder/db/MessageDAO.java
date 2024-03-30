package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME_MESSAGES;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDAO {
    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " AS m1 WHERE receiverID = (SELECT receiverID FROM " + DB_TABLE_NAME_MESSAGES + " AS m2 WHERE m1.receiverID = m2.receiverID) ORDER BY timestamp DESC")
    LiveData<List<Message>> getGroupedMessages();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM " + DB_TABLE_NAME_MESSAGES)
    void deleteAll();

    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " WHERE synced = 0")
    List<Message> getUnsyncedMessages(); // Get unsynced messages

    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " WHERE receiverID = :receiverID")
    List<Message> getMessagesByReceiverID(String receiverID);
}
