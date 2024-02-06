package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_DOSAGE_FOR;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_FORM;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NAME;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_NOTE;
import static com.evans.pillreminder.helpers.Constants.DB_COLUMN_MEDICATION_START_DATE;
import static com.evans.pillreminder.helpers.Constants.DB_TABLE_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DB_TABLE_NAME)
public class Medication {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_NAME)
    private String medicationName;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_FORM)
    private String medicationForm;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_DOSAGE)
    private String medicationDosage;
    @NonNull
    @ColumnInfo(name = DB_COLUMN_MEDICATION_START_DATE)
    private String medicationDate; // TODO: use date object instead
    @Nullable
    @ColumnInfo(name = DB_COLUMN_MEDICATION_NOTE)
    private String medicationNote;
    @Nullable
    @ColumnInfo(name = DB_COLUMN_MEDICATION_DOSAGE_FOR)
    private String medicationFor; // x days, x weeks, x months
    public Medication(@NonNull String medicationName, @NonNull String medicationForm,
                      @NonNull String medicationDosage, @NonNull String medicationDate,
                      @Nullable String medicationNote, @NonNull String medicationFor) {
        this.medicationName = medicationName;
        this.medicationForm = medicationForm;
        this.medicationDosage = medicationDosage;
        this.medicationDate = medicationDate;
        this.medicationNote = medicationNote;
        this.medicationFor = medicationFor;
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
