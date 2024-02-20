package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE_FOR;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FORM;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NAME;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NOTE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_REMINDER_TIME;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_START_DATE;
import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME;

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
@Entity(tableName = DB_TABLE_NAME)
public class Medication {

    @Exclude
    @ColumnInfo(name = DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID)
    String documentId; // Firestore DocumentID

    @PrimaryKey(autoGenerate = true)
    private int id;
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
        return medication;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedId() {
        return id;
    }

    public void setMedId(int id) {
        this.id = id;
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
}
