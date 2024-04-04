package com.evans.pillreminder.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicationViewModel extends AndroidViewModel {
    private final LiveData<List<Medication>> allMedications;
    private LiveData<List<Medication>> allHistoryMedications;
    private final MedicationRepository medicationRepository;

    public MedicationViewModel(Application application) {
        super(application);
        medicationRepository = new MedicationRepository(application);
        allMedications = medicationRepository.getAllMedications();
    }

    public LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    public void insert(Medication medication) {
        medicationRepository.insert(medication);
    }

    public void update(Medication medication) {
        medicationRepository.update(medication);
    }

    public void delete(Medication medication) {
        medicationRepository.delete(medication);
    }

    public void deleteAll(Medication medication) {
        medicationRepository.deleteAll();
    }

    public void updateTaken(Medication medication) {
        medicationRepository.updateTaken(medication);
    }

    public void resolveMedication(Medication medication) {
        medicationRepository.resolveMedication(medication);
    }

    public LiveData<List<Medication>> getAllHistoryMedications() {
        return medicationRepository.getAllHistoryMedications();
    }
}
