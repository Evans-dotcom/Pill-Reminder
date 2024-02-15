package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_REMINDER_TIME;
import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicationDAO {
    @Query("SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + DB_COLUMN_MEDICATION_REMINDER_TIME + " ASC")
    LiveData<List<Medication>> getAllMedications();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Medication medication);

    @Update
    void update(Medication medication);

    @Delete
    void delete(Medication medication);

    @Query("DELETE FROM " + DB_TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + DB_TABLE_NAME + " WHERE synced = 0")
    List<Medication> getUnsyncedMedications(); // Get unsynced medications

    // Additional methods for syncing with Firestore
    @Query("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID + " = :documentId")
    Medication getMedicationByDocumentId(String documentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Medication medication); // Insert or update medication record based on documentId
}
