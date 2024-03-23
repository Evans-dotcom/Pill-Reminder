package com.evans.pillreminder.services;

import static com.evans.pillreminder.helpers.Constants.CHANNEL_DOCTOR_NOTIFICATION_ID;
import static com.evans.pillreminder.helpers.Constants.NOTIFICATION_CHANNEL_DOCTOR_ID;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evans.pillreminder.R;

public class NotificationHandler {
    Context context;
    NotificationManagerCompat notificationManagerCompat;
    private String doctorNotificationTitle;
    private String doctorNotificationMessage;

    public NotificationHandler(Context context) {
        this.context = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public void setDoctorNotificationTitle(String doctorNotificationTitle) {
        this.doctorNotificationTitle = doctorNotificationTitle;
    }

    public void setDoctorNotificationMessage(String doctorNotificationMessage) {
        this.doctorNotificationMessage = doctorNotificationMessage;
    }

    public void notificationFromDoctor() {
        Notification offlineNotification = new NotificationCompat.Builder(context, CHANNEL_DOCTOR_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.capsule_right_svg)
                .setContentTitle(doctorNotificationTitle)
                .setContentText(doctorNotificationMessage)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        notificationManagerCompat.notify(NOTIFICATION_CHANNEL_DOCTOR_ID, offlineNotification);
    }
}
