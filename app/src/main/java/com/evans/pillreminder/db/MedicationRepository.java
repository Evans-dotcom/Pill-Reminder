package com.evans.pillreminder.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicationRepository {
    private MedicationDAO medicationDAO;
    private LiveData<List<Medication>> allMedications;

    MedicationRepository(Application application) {
        MedicationDatabase medDB = MedicationDatabase.getInstance(application);
        medicationDAO = medDB.medicationDAO();
        allMedications = medicationDAO.getAllMedications();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
    public void insert(Medication medication) {
        new InsertAsyncTask(medicationDAO).execute(medication);
    }

    public void update(Medication medication) {
        new UpdateAsyncTask(medicationDAO).execute(medication);
    }

    public void delete(Medication medication) {
        new DeleteAsyncTask(medicationDAO).execute(medication);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(medicationDAO).execute();
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
            medicationDAO.delete(medications[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Medication, Void, Void> {
        private MedicationDAO medicationDAO;

        public DeleteAllAsyncTask(MedicationDAO medicationDAO) {
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
            medicationDAO.deleteAll();
            return null;
        }
    }
}
