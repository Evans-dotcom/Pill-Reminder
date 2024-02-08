package com.evans.pillreminder.helpers;

public class Medication {
    private String medicineName, medicineForm, medicineDose, medReminderTime, note;

    public Medication(String medicineName, String medicineForm, String medicineDose, String medReminderTime, String note) {
        this.medicineName = medicineName;
        this.medicineForm = medicineForm;
        this.medicineDose = medicineDose;
        this.medReminderTime = medReminderTime;
        this.note = note;
    }

    public Medication(String medicineName, String medicineForm, String medicineDose, String medReminderTime) {
        this.medicineName = medicineName;
        this.medicineForm = medicineForm;
        this.medicineDose = medicineDose;
        this.medReminderTime = medReminderTime;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineForm() {
        return medicineForm;
    }

    public void setMedicineForm(String medicineForm) {
        this.medicineForm = medicineForm;
    }

    public String getMedicineDose() {
        return medicineDose;
    }

    public void setMedicineDose(String medicineDose) {
        this.medicineDose = medicineDose;
    }

    public String getMedReminderTime() {
        return medReminderTime;
    }

    public void setMedReminderTime(String medReminderTime) {
        this.medReminderTime = medReminderTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
