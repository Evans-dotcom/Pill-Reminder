package com.evans.pillreminder.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicationViewModel extends AndroidViewModel {
    private final LiveData<List<Medication>> allMedications;
    private MedicationRepository medicationRepository;

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
}
