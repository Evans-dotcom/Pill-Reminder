package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_SYNCED;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_REMINDER_TIME;
import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME_MEDICATIONS;

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
    @Query("SELECT * FROM " + DB_TABLE_NAME_MEDICATIONS + " ORDER BY " + DB_COLUMN_MEDICATION_REMINDER_TIME + " ASC")
    LiveData<List<Medication>> getAllMedications();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Medication medication);

    @Update
    void update(Medication medication);

    @Delete
    void delete(Medication medication);

    @Query("DELETE FROM " + DB_TABLE_NAME_MEDICATIONS)
    void deleteAll();

    @Query("SELECT * FROM " + DB_TABLE_NAME_MEDICATIONS + " WHERE " + DB_COLUMN_MEDICATION_FIRESTORE_SYNCED + " = 0 AND " + DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID + " = 'EMPTY' ORDER BY " + DB_COLUMN_MEDICATION_REMINDER_TIME + " ASC")
    List<Medication> getUnsyncedMedications(); // Get unsynced medications

    // Additional methods for syncing with Firestore
    @Query("SELECT * FROM " + DB_TABLE_NAME_MEDICATIONS + " WHERE " + DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID + " = :documentId")
    Medication getMedicationByDocumentId(String documentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Medication medication); // Insert or update medication record based on documentId
}
