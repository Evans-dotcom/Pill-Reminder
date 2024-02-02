package com.evans.pillreminder.db;

import static com.evans.pillreminder.helpers.Constants.DB_ROOM_DB_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medication.class}, version = 1, exportSchema = false)
public abstract class MedicationRoomDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile MedicationRoomDatabase INSTANCE;

    static MedicationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MedicationRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MedicationRoomDatabase.class, DB_ROOM_DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MedicationDAO medicationDAO();
}
