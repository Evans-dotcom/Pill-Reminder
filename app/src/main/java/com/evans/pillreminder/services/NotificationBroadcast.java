package com.evans.pillreminder.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NotificationBroadcast extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.filterEquals(new Intent("com.evans.pillreminder.services.fcm_notification"))) {
            //
        }
    }
}
