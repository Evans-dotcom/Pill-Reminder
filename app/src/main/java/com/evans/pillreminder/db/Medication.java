package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE_FOR;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_SYNCED;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FORM;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NAME;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NOTE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_REMINDER_TIME;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_START_DATE;
import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME_MEDICATIONS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Create a table in the SQLite database named DB_TABLE_NAME
 */
@IgnoreExtraProperties
@Entity(tableName = DB_TABLE_NAME_MEDICATIONS)
public class Medication {

    @Exclude
    @ColumnInfo(name = DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID)
    String documentId; // Firestore DocumentID

    @PrimaryKey(autoGenerate = true)
    private int medicationID;
    @ColumnInfo(name = DB_COLUMN_MEDICATION_FIRESTORE_SYNCED)
    private boolean synced; // Flag to track sync status

    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_NAME)
    private String medicationName;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_FORM)
    private String medicationForm; /* tablet, liquids etc*/
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_DOSAGE)
    private String medicationDosage;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_START_DATE)
    private String medicationDate; // TODO: use date object instead

    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_REMINDER_TIME)
    private String medicationReminderTime; // TODO: use date object instead
    @Nullable
    @ColumnInfo(name = DB_COLUMN_MEDICATION_NOTE)
    private String medicationNote;
    @Nullable
    @ColumnInfo(name = DB_COLUMN_MEDICATION_DOSAGE_FOR)
    private String medicationFor; // x days, x weeks, x months
    private boolean resolved = false;
    private boolean todayTaken = false;
    private String updatedAt;

    public Medication() {
        // empty constructor for firebase firestore
        // Default constructor required for calls to DataSnapshot.getValue(Medication.class)
    }

    public Medication(@NonNull String medicationName, @NonNull String medicationForm,
                      @NonNull String medicationDosage, @NonNull String medicationDate,
                      @NonNull String medicationReminderTime, @NonNull String medicationFor,
                      @Nullable String medicationNote) {
        this.medicationName = medicationName;
        this.medicationForm = medicationForm;
        this.medicationDosage = medicationDosage;
        this.medicationDate = medicationDate;
        this.medicationReminderTime = medicationReminderTime;
        this.medicationFor = medicationFor;
        this.medicationNote = medicationNote;
        synced = false; // By default, medication is not synced
        this.documentId = "EMPTY";
    }

    // Static method to create Medication object from a Firestore document snapshot
    public static Medication fromSnapshot(DocumentSnapshot snapshot) {
        Medication medication = new Medication();
        medication.setDocumentId(snapshot.getId());
        medication.setMedicationName(Objects.requireNonNull(snapshot.getString("medicationName")));
        medication.setMedicationForm(Objects.requireNonNull(snapshot.getString("medicationForm")));
        medication.setMedicationDosage(Objects.requireNonNull(snapshot.getString("medicationDosage")));
        medication.setMedicationDate(Objects.requireNonNull(snapshot.getString("medicationDate")));
        medication.setMedicationReminderTime(Objects.requireNonNull(snapshot.getString("medicationReminderTime")));
        medication.setMedicationFor(Objects.requireNonNull(snapshot.getString("medicationFor")));
        medication.setMedicationNote(snapshot.getString("medicationNote"));
        medication.setResolved(Boolean.TRUE.equals(snapshot.getBoolean("medicationResolved")));
        medication.setTodayTaken(Boolean.TRUE.equals(snapshot.getBoolean("todayTaken")));
        medication.setUpdatedAt(snapshot.getString("updatedAt"));
        return medication;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @NonNull
    @Override
    public String toString() {
//        return super.toString();
        return "Medication{" +
                "documentId='" + documentId + '\'' +
                ", mID=" + medicationID +
                ", synced=" + synced +
                ", medicationName='" + medicationName + '\'' +
                ", medicationForm='" + medicationForm + '\'' +
                ", medicationDosage='" + medicationDosage + '\'' +
                ", medicationDate='" + medicationDate + '\'' +
                ", medicationReminderTime='" + medicationReminderTime + '\'' +
                ", medicationNote='" + medicationNote + '\'' +
                ", medicationFor='" + medicationFor + '\'' +
                ", resolved='" + resolved + '\'' +
                '}';
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    // Method to convert Medication object to a Map for Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("medicationName", medicationName);
        map.put("medicationForm", medicationForm);
        map.put("medicationDosage", medicationDosage);
        map.put("medicationDate", medicationDate);
        map.put("medicationReminderTime", medicationReminderTime);
        map.put("medicationFor", medicationFor);
        map.put("medicationNote", medicationNote);
        map.put("medicationResolved", resolved);
        map.put("todayTaken", todayTaken);
        map.put("updatedAt", updatedAt);
        return map;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @NonNull
    public String getMedicationReminderTime() {
        return medicationReminderTime;
    }

    public void setMedicationReminderTime(@NonNull String medicationReminderTime) {
        this.medicationReminderTime = medicationReminderTime;
    }

    public int getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(int medicationID) {
        this.medicationID = medicationID;
    }

    public int getMedId() {
        return medicationID;
    }

    public void setMedId(int id) {
        this.medicationID = id;
    }

    @NonNull
    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(@NonNull String medicationName) {
        this.medicationName = medicationName;
    }

    @NonNull
    public String getMedicationForm() {
        return medicationForm;
    }

    public void setMedicationForm(@NonNull String medicationForm) {
        this.medicationForm = medicationForm;
    }

    @NonNull
    public String getMedicationDosage() {
        return medicationDosage;
    }

    public void setMedicationDosage(@NonNull String medicationDosage) {
        this.medicationDosage = medicationDosage;
    }

    @NonNull
    public String getMedicationDate() {
        return medicationDate;
    }

    public void setMedicationDate(@NonNull String medicationDate) {
        this.medicationDate = medicationDate;
    }

    @Nullable
    public String getMedicationNote() {
        return medicationNote;
    }

    public void setMedicationNote(@Nullable String medicationNote) {
        this.medicationNote = medicationNote;
    }

    @Nullable
    public String getMedicationFor() {
        return medicationFor;
    }

    public void setMedicationFor(@Nullable String medicationFor) {
        this.medicationFor = medicationFor;
    }

    public boolean isTodayTaken() {
        return todayTaken;
    }

    public void setTodayTaken(boolean todayTaken) {
        this.todayTaken = todayTaken;
    }
}
