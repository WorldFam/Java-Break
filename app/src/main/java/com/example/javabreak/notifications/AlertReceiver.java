package com.example.javabreak.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        String name = intent.getStringExtra ("name");
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(name);
        notificationHelper.getManager().notify(1, nb.build());
    }
}