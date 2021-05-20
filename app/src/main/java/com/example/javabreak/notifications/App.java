package com.example.javabreak.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String GROUP_1_ID = "Notification Channel Group 1";
    public static final String GROUP_2_ID = "Notification Channel Group 2";
    public static final String GROUP_3_ID = "Notification Channel Group 3";
    public static final String GROUP_4_ID = "Notification Channel Group 4";
    public static final String GROUP_5_ID = "Notification Channel Group 5";

    public static final String CHANNEL_1_ID = "Notification Channel Default";
    public static final String CHANNEL_2_ID = "Notification Channel Led";
    public static final String CHANNEL_3_ID = "Notification Channel Vibration";
    public static final String CHANNEL_4_ID = "Notification Channel Led Vibration";

    public static final String CHANNEL_5_ID = "Notification Channel Sound 1";
    public static final String CHANNEL_6_ID = "Notification Channel Sound 2";
    public static final String CHANNEL_7_ID = "Notification Channel Sound 3";
    public static final String CHANNEL_8_ID = "Notification Channel Sound 4";
    public static final String CHANNEL_9_ID = "Notification Channel Sound 5";

    public static final String CHANNEL_10_ID = "Notification Channel Sound 1 Led";
    public static final String CHANNEL_11_ID = "Notification Channel Sound 2 Led";
    public static final String CHANNEL_12_ID = "Notification Channel Sound 3 Led";
    public static final String CHANNEL_13_ID = "Notification Channel Sound 4 Led";
    public static final String CHANNEL_14_ID = "Notification Channel Sound 5 Led";

    public static final String CHANNEL_15_ID = "Notification Channel Sound 1 Vibration";
    public static final String CHANNEL_16_ID = "Notification Channel Sound 2 Vibration";
    public static final String CHANNEL_17_ID = "Notification Channel Sound 3 Vibration";
    public static final String CHANNEL_18_ID = "Notification Channel Sound 4 Vibration";
    public static final String CHANNEL_19_ID = "Notification Channel Sound 5 Vibration";

    public static final String CHANNEL_20_ID = "Notification Channel Sound 1 Led Vibration";
    public static final String CHANNEL_21_ID = "Notification Channel Sound 2 Led Vibration";
    public static final String CHANNEL_22_ID = "Notification Channel Sound 3 Led Vibration";
    public static final String CHANNEL_23_ID = "Notification Channel Sound 4 Led Vibration";
    public static final String CHANNEL_24_ID = "Notification Channel Sound 5 Led Vibration";

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
