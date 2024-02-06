package com.evans.pillreminder.db;

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
    @Query("SELECT * FROM " + DB_TABLE_NAME + " ORDER BY id ASC")
    LiveData<List<Medication>> getAllMedications();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Medication medication);

    @Update
    void update(Medication medication);

    @Delete
    void delete(Medication medication);

    @Query("DELETE FROM " + DB_TABLE_NAME)
    void deleteAll();
}
