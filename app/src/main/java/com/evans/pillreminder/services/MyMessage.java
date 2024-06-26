package com.evans.pillreminder.services;

import static com.evans.pillreminder.helpers.Constants.CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID;
import static com.evans.pillreminder.helpers.Constants.DB_FIRESTORE_FIELD_USER_TOKEN;
import static com.evans.pillreminder.helpers.Constants.FILENAME_TOKEN_JSON;
import static com.evans.pillreminder.helpers.Constants.FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID;
import static com.evans.pillreminder.helpers.Constants.MY_TAG;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveDictionary;
import static com.evans.pillreminder.helpers.UtilityFunctions.saveTokenToFirestore;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evans.pillreminder.MainActivity;
import com.evans.pillreminder.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessage extends FirebaseMessagingService {
    private Context context;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        context = this;
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(MY_TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token1 = task.getResult();

                    Log.d(MY_TAG, "TOKEN: " + token1);
                    Toast.makeText(context, token1, Toast.LENGTH_SHORT).show();
                    Map<String, Object> data = Map.of(DB_FIRESTORE_FIELD_USER_TOKEN, token1);

                    saveDictionary((Activity) context, data, FILENAME_TOKEN_JSON);
                    saveTokenToFirestore(token);
                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // show notification
        // FIXME: show 'New Message' if no title
        Log.i(MY_TAG, "MessageReceived:\n\n\n\n" + message + "\n\n\n\n");
        showNotification(message);
        // trigger broadcast receiver
    }

    @Override
    public void onSendError(@NonNull String msgId, @NonNull Exception exception) {
        super.onSendError(msgId, exception);
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
    }

    private void showNotification(RemoteMessage msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_FIREBASE_CLOUD_MESSAGING_NOTIFICATION_ID)
                .setContentTitle(msg.getNotification().getTitle())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.capsule_right_svg)
                .setContentText(msg.getNotification().getBody());

        Intent intent = new Intent(this, MainActivity.class);

        String senderId = msg.getSenderId();
        long sentTime = msg.getSentTime();
        String messageId = msg.getMessageId();
        String body = msg.getNotification().getBody();
//        msg.getFrom();

        intent.putExtra("senderID", senderId);
        intent.putExtra("sentTime", sentTime);
        intent.putExtra("messageID", messageId);
        intent.putExtra("message", body);
        intent.putExtra("msg", msg);

//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 999, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i(MY_TAG, msg.getNotification().getTitle() + " >BODY: " + msg.getNotification().getBody());
        manager.notify(FIREBASE_CLOUD_MESSAGING_NOTIFICATION_POP_UP_ID, builder.build());
    }
}
