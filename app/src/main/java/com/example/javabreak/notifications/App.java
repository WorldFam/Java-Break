package com.example.javabreak.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "Notification Channel Default";
    public static final String CHANNEL_2_ID = "Notification Channel Led";
    public static final String CHANNEL_3_ID = "Notification Channel Vibration";
    public static final String CHANNEL_4_ID = "Notification Channel Led and Vibration";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel ();
    }
    private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_1_ID,
                        "Notification Channel",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("Notification Channel");
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel1);
            }
    }



}
