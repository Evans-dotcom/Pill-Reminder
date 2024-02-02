package com.evans.pillreminder.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicationRepository {
    private MedicationDAO medicationDAO;
    private LiveData<List<Medication>> allMedications;

    MedicationRepository(Application application) {
        MedicationRoomDatabase db = MedicationRoomDatabase.getDatabase(application);
        medicationDAO = db.medicationDAO();
        allMedications = medicationDAO.getAllMedications();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
    void insert(Medication medication) {
        MedicationRoomDatabase.databaseWriteExecutor.execute(() -> {
            medicationDAO.insert(medication);
        });
    }
}
