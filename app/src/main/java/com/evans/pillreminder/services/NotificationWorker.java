package com.evans.pillreminder.services;

import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.Constants.OFFLINE_NOTIFICATION_REMINDER_ID;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.evans.pillreminder.MainActivity;
import com.evans.pillreminder.R;
import com.evans.pillreminder.db.Medication;
import com.evans.pillreminder.db.MedicationViewModel;
import com.evans.pillreminder.helpers.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.annotations.NonNull;

public class NotificationWorker extends Worker {

    private final LiveData<List<Medication>> allMedications;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        MedicationViewModel medicationViewModel = new MedicationViewModel((Application) this.getApplicationContext());
        allMedications = medicationViewModel.getAllMedications();
    }

    @androidx.annotation.NonNull
    @NonNull
    @Override
    public Result doWork() {
//        return null;
        List<Medication> medications = allMedications.getValue();
        Log.i(MY_TAG, "doWork: is running in the background: " + (medications == null));

        if (medications != null && !medications.isEmpty()) {
            Log.i(MY_TAG, "NotificationWorker: " + medications);
            for (Medication medication : medications) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {
                    long timeMillis = Objects.requireNonNull(format.parse(medication.getMedicationReminderTime())).getTime();

                    if (timeMillis <= System.currentTimeMillis()) {
                        triggerNotification(this.getApplicationContext());
                    }
                } catch (ParseException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

    private void triggerNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.OFFLINE_NOTIFICATION_REMINDER_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            // ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                        int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // notificationId is a unique int for each notification that you must define.
//        notify(NOTIFICATION_ID, builder.build());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Integer.parseInt(OFFLINE_NOTIFICATION_REMINDER_ID), builder.build());
    }
}
