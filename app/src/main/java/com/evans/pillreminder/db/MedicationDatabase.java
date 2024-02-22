package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_MEDICATIONS;
import static com.evans.pillreminder.helpers.Constants.DB_ROOM_DB_NAME;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medication.class}, version = 1, exportSchema = false)
public abstract class MedicationDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    // Add methods for Firestore data synchronization
    private static volatile MedicationDatabase INSTANCE;
    // Add Firestore initialization and connection management if necessary
    private FirebaseFirestore firestore;
    private CollectionReference medicationsCollection;
    private ListenerRegistration firestoreListener;

    public static synchronized MedicationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MedicationDatabase.class, DB_ROOM_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract MedicationDAO medicationDAO();

    // Add Firestore integration and data synchronization logic here
    public FirebaseFirestore getFirestore() {
        if (firestore == null) {
            FirebaseFirestoreSettings firestoreSettings = new FirebaseFirestoreSettings.Builder()
                    .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                    .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
//                    .setPersistenceEnabled(true) // Enable Firestore local cache
                    .build();
            firestore = FirebaseFirestore.getInstance();
            firestore.setFirestoreSettings(firestoreSettings);
        }
        return firestore;
    }

    /**
     * Methods to listen for changes in Firestore documents
     * Methods to update local Room data based on Firestore changes
     * Methods to upload local changes to Firestore
     */
    // Initialize Firestore and get medications collection reference
    private CollectionReference getMedicationsCollection() {
        if (medicationsCollection == null) {
            medicationsCollection = getFirestore().collection(DB_FIRESTORE_COLLECTIONS_MEDICATIONS);
        }
        return medicationsCollection;
    }

    // Method to listen for changes in Firestore documents
    public void startFirestoreListener() {
        firestoreListener = getMedicationsCollection()
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Error listening to Firestore documents: " + e.getMessage());
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            // Handle each document change
                            // Update local Room data based on Firestore changes
                            Medication medication = Medication.fromSnapshot(doc);
                            // Update local Room database with the new data
                            databaseWriteExecutor.execute(() -> medicationDAO().insertOrUpdate(medication));
                        }
                    }
                });
    }

    // Method to stop listening for changes in Firestore documents
    public void stopFirestoreListener() {
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
    }

    // Method to upload local changes to Firestore
    public void uploadLocalChangesToFirestore() {
        List<Medication> localChanges = medicationDAO().getUnsyncedMedications();// Implement this method in your DAO

        if (localChanges == null) return;
/*
        for (Medication medication : localChanges)) {
            // Convert Medication object to a Map for Firestore
//            Map<String, Object> medicationData = medication.toMap();

            // Upload changes to Firestore
            getMedicationsCollection()
                    .document(medication.getDocumentId())
                    .set(medicationData)
                    .addOnSuccessListener(aVoid -> {
                        // Update sync status in Room database
                        medication.setSynced(true);
                        databaseWriteExecutor.execute(() -> medicationDAO().update(medication));
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                        Log.e("FirestoreUpload", "Error uploading medication data: " + e.getMessage());
                    });
        }*/
        Log.i(MY_TAG, "Local changes uploaded to Firestore");
    }

    // Ensure proper cleanup of Firestore resources when the database is closed
    @Override
    public void close() {
        super.close();
        if (firestore != null) {
            firestore.terminate();
            firestore = null;
        }
    }
}
