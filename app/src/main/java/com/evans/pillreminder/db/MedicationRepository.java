package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_COLLECTIONS_MEDICATIONS;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicationRepository {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //    private final LiveData<List<Medication>> localChanges;
    private MedicationDAO medicationDAO;
    private LiveData<List<Medication>> allMedications;
    private CollectionReference medicationsCollection;
    private FirebaseFirestore firestore;

    MedicationRepository(Application application) {
        MedicationDatabase medDB = MedicationDatabase.getInstance(application);
        medicationDAO = medDB.medicationDAO();
        allMedications = medicationDAO.getAllMedications();
    }

//    public LiveData<List<Medication>> getLocalChanges() {
//        return localChanges;
//    }

    private CollectionReference getMedicationsCollection() {
        if (medicationsCollection == null) {
            medicationsCollection = getFirestore().collection(DB_FIRESTORE_COLLECTIONS_MEDICATIONS);
        }
        return medicationsCollection;
    }

    public FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
    public void insert(Medication medication) {
        // Update sync status to false before updating the record
        medication.setSynced(false);
        new InsertAsyncTask(medicationDAO).execute(medication);
        // Trigger synchronization with Firestore after local insert
        uploadLocalChangesToFirestore();
    }

    public void update(Medication medication) {
        // Update sync status to false before updating the record
        medication.setSynced(false);
        new UpdateAsyncTask(medicationDAO).execute(medication);
        uploadLocalChangesToFirestore();
    }

    public void delete(Medication medication) {
        // Update sync status to false before updating the record
        medication.setSynced(false);
        new DeleteAsyncTask(medicationDAO).execute(medication);

        // Trigger synchronization with Firestore after local delete
        uploadLocalChangesToFirestore();
    }

    // Method to upload local changes to Firestore
    private void uploadLocalChangesToFirestore() {
        // Retrieve unsynced medications from local Room database
//        LiveData<List<Medication>> localChanges = medicationDAO.getUnsyncedMedications();
//        Log.i(MY_TAG, "Local Changes: " + localChanges.getValue() + " INITIALIZED: " + localChanges.isInitialized());
        // TODO: For this to work, update the sync status of each medication after successful db operation
//        if (localChanges.getValue() == null) return;
        // Upload local changes to Firestore
        // Implement this logic similar to MedicationDatabase.uploadLocalChangesToFirestore()
//        for (Medication medication : localChanges.getValue()) {
        // Convert Medication object to a Map for Firestore
//            Map<String, Object> medicationData = medication.toMap();

        // Upload changes to Firestore
//            getMedicationsCollection()
//                    .document(medication.getDocumentId())
//                    .set(medicationData)
//                    .addOnSuccessListener(aVoid -> {
        // Update sync status in Room database
//                        medication.setSynced(true);
//                        databaseWriteExecutor.execute(() -> medicationDAO.insertOrUpdate(medication));
//                    })
//                    .addOnFailureListener(e -> {
        // Handle upload failure
//                        Log.e("FirestoreUpload", "Error uploading medication data: " + e.getMessage());
//                    });
//        }
//        Log.i(MY_TAG, "Local changes uploaded to Firestore");
        UploadUnsyncData.OnTaskCompleteListener<List<Medication>> listener = new UploadUnsyncData.OnTaskCompleteListener<List<Medication>>() {
            @Override
            public void onTaskComplete(List<Medication> result) {
                Log.i(MY_TAG, "onTaskComplete: " + result.get(0));
            }
        };
        new UploadUnsyncData(medicationDAO, getFirestore(), listener).execute();
    }

    public void deleteAll() {
        // Set synced status to false before deleting all
        List<Medication> allMedications = getAllMedications().getValue();
        if (allMedications != null) {
            for (Medication medication : allMedications) {
                medication.setSynced(false);
            }
        }
        medicationDAO.deleteAll();

        new DeleteAllAsyncTask(medicationDAO).execute();
        // Trigger synchronization with Firestore after local delete // TODO: Add isDeleted fields in Firebase
        uploadLocalChangesToFirestore();
    }

    private static class InsertAsyncTask extends AsyncTask<Medication, Void, Void> {
        private MedicationDAO medicationDAO;

        public InsertAsyncTask(MedicationDAO medicationDAO) {
            this.medicationDAO = medicationDAO;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This will normally run on a background thread. But to better
         * support testing frameworks, it is recommended that this also tolerates
         * direct execution on the foreground thread, as part of the {@link #execute} call.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param medications The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Medication... medications) {
            medicationDAO.insert(medications[0]);
            Log.w("M_Tag", "doInBackground: " + medications[0].getMedicationName());
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Medication, Void, Void> {
        private MedicationDAO medicationDAO;

        public UpdateAsyncTask(MedicationDAO medicationDAO) {
            this.medicationDAO = medicationDAO;
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This will normally run on a background thread. But to better
         * support testing frameworks, it is recommended that this also tolerates
         * direct execution on the foreground thread, as part of the {@link #execute} call.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param medications The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Medication... medications) {
            medicationDAO.update(medications[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Medication, Void, Void> {
        private MedicationDAO medicationDAO;

        public DeleteAsyncTask(MedicationDAO medicationDAO) {
            this.medicationDAO = medicationDAO;
        }

        @Override
        protected Void doInBackground(Medication... medications) {
            medicationDAO.delete(medications[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Medication, Void, Void> {
        private MedicationDAO medicationDAO;

        public DeleteAllAsyncTask(MedicationDAO medicationDAO) {
            this.medicationDAO = medicationDAO;
        }

        @Override
        protected Void doInBackground(Medication... medications) {
            medicationDAO.deleteAll();
            return null;
        }
    }

    private static class UploadUnsyncData extends AsyncTask<Void, Void, List<Medication>> {
        private final FirebaseFirestore firebaseFirestore;
        private final OnTaskCompleteListener<List<Medication>> listener;
        private MedicationDAO medicationDAO;
        private CollectionReference medicationsCollection;

        public UploadUnsyncData(MedicationDAO medicationDAO, FirebaseFirestore firebaseFirestore, OnTaskCompleteListener<List<Medication>> listener) {
            this.medicationDAO = medicationDAO;
            this.firebaseFirestore = firebaseFirestore;
            this.listener = listener;
        }

        @Override
        protected List<Medication> doInBackground(Void... voids) {
            List<Medication> unsyncedMedications = medicationDAO.getUnsyncedMedications();
            Log.i(MY_TAG, "doInBackground: " + unsyncedMedications.size());
            return unsyncedMedications;
        }

        private CollectionReference getMedicationsCollection() {
            if (medicationsCollection == null) {
                medicationsCollection = firebaseFirestore.collection(DB_FIRESTORE_COLLECTIONS_MEDICATIONS);
            }
            Log.i(MY_TAG, "getMedicationsCollection: " + medicationsCollection.getPath());
            return medicationsCollection;
        }

        public MedicationDAO medicationDAO() {
            return medicationDAO;
        }

        @Override
        protected void onPostExecute(List<Medication> medications) {
//            super.onPostExecute(medications);
            for (Medication medication : medications) {
                DocumentReference mDocument = getMedicationsCollection().document();

                mDocument.set(medication.toMap())
                        .addOnSuccessListener(aVoid -> {
                            // Update sync status in Room database
                            medication.setDocumentId(mDocument.getId());
                            medication.setSynced(true);
                            databaseWriteExecutor.execute(() -> {
                                medicationDAO().update(medication);
                                medicationDAO.update(medication);
                            });
                        });
            }

            Log.i(MY_TAG, "onPostExecute: Local changes uploaded to Firestore");
        }

        public interface OnTaskCompleteListener<T> {
            void onTaskComplete(T result);
        }
    }
}
