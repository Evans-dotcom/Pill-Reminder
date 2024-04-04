package com.evans.pillreminder.helpers;

public class Constants {
    public static final String MY_TAG = "M_Tag";
    public static final String[] DAYS_OF_WEEK = {"SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
    public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    // Database
    public static final String DB_ROOM_DB_NAME = "medication_database";
    public static final String DB_TABLE_NAME_MEDICATIONS = "medication";
    public static final String DB_TABLE_NAME_MESSAGES = "messages";
    public static final String DB_USER_TABLE_NAME = "user";
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
    public static final String DB_FIRESTORE_COLLECTIONS_USERS = "/users";
    public static final String DB_FIRESTORE_COLLECTIONS_MESSAGES = "/messages";
    public static final String DB_FIRESTORE_SUB_COLLECTIONS_MESSAGE = "/message";
    public static final String DB_FIRESTORE_FIELD_USER_TOKEN = "fcmToken";

    // Notifications
    public final static String OFFLINE_NOTIFICATIONS_REMINDER_NAME = "offline_medication_reminder_name";
    public final static String OFFLINE_NOTIFICATIONS_REMINDER_DESCRIPTION = "offline_medication_reminder_name";
    public final static String CHANNEL_DOCTOR_NOTIFICATION_ID = "doctor_notification";
    public final static int NOTIFICATION_CHANNEL_DOCTOR_ID = 1;
    public final static String CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID = "fcm_notification";
    public final static int FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID = 444;
    public final static String CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID_INCOMING = "fcm_notification_incoming";
    public final static int FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID_INCOMING = 555;
    // Topic
    public static final String FCM_TOPIC_UPDATES = "pills_updates";
    // Filenames
    public static final String FILENAME_TOKEN_JSON = "FCMToken.json";
    public static final String FILENAME_USER_DETAILS_JSON = "user.json";
    public final static String OFFLINE_NOTIFICATION_REMINDER_ID = "100";
}
