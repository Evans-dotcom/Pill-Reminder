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
    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES)
    LiveData<List<Message>> getGroupedMessages();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    @Query("INSERT OR IGNORE INTO "+DB_TABLE_NAME_MESSAGES+ "()")
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM " + DB_TABLE_NAME_MESSAGES)
    void deleteAll();

    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " WHERE synced = 0")
    List<Message> getUnsyncedMessages(); // Get unsynced messages

    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " WHERE (receiverID = :receiverID and senderID = :userID) or (receiverID = :userID and senderID = :receiverID)")
    List<Message> getMessagesByReceiverID(String userID, String receiverID);

    @Query("SELECT * FROM " + DB_TABLE_NAME_MESSAGES + " GROUP BY receiverID")
    List<Message> getGroupedMessagesList();
}
