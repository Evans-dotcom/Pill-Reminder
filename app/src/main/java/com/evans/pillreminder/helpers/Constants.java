package com.evans.pillreminder.helpers;

public class Constants {
    public static final String MY_TAG = "M_Tag";
    public static final String[] DAYS_OF_WEEK = {"SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
    public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    // Database
    public static final String DB_ROOM_DB_NAME = "medication_database";
    public static final String DB_TABLE_NAME = "medication";
    public static final String DB_COLUMN_MEDICATION_NAME = "med_name";
    public static final String DB_COLUMN_MEDICATION_FIRESTORE_DOCUMENT_ID = "med_document_id";
    public static final String DB_COLUMN_MEDICATION_FORM = "med_form";
    public static final String DB_COLUMN_MEDICATION_DOSAGE = "med_dosage";
    public static final String DB_COLUMN_MEDICATION_START_DATE = "med_start_date";
    public static final String DB_COLUMN_MEDICATION_REMINDER_TIME = "med_reminder_time";
    public static final String DB_COLUMN_MEDICATION_FIRESTORE_SYNCED = "med_firestore_synced";
    public static final String DB_COLUMN_MEDICATION_NOTE = "med_note";
    public static final String DB_COLUMN_MEDICATION_DOSAGE_FOR = "med_duration"; // prescription duration
    public static final String DB_FIRESTORE_COLLECTIONS_MEDICATIONS = "/medications";
//    public static final String DB_COLUMN_MEDICATION_
//    public static final String DB_COLUMN_MEDICATION_
//    public static final String DB_COLUMN_MEDICATION_
//    public static final String DB_COLUMN_MEDICATION_
//    public static final String DB_COLUMN_MEDICATION_
}
