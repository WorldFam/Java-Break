package com.example.javabreak.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.javabreak.R;
import com.example.javabreak.MainActivity;

public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_0_ID = "Notification Channel";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        createChannel();

    }
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_0_ID,
                "Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("Notification Channel");
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification(String name) {

        Intent activityIntent = new Intent(getApplicationContext (), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext () ,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_0_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle(name)
                .setContentText("Work session session has started!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
    }
}