package com.example.javabreak.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.javabreak.notifications.App.CHANNEL_1_ID;
import static com.example.javabreak.notifications.App.CHANNEL_2_ID;
import static com.example.javabreak.notifications.App.CHANNEL_3_ID;
import static com.example.javabreak.notifications.App.CHANNEL_4_ID;

public class NotificationReceiver extends BroadcastReceiver {
    boolean vibration,led;
    Vibrator vibrator;
    NotificationChannel notificationChannel;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context ,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String key = intent.getStringExtra ("timerState");

        vibration = intent.getBooleanExtra ("vibration",false);
        led = intent.getBooleanExtra ("led",false);

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        vibration = sharedPreferences.getBoolean ("vibration",false);
        led = sharedPreferences.getBoolean ("led",false);

        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        Log.d("STATE",String.valueOf (vibration) + " Vibration");

        notificationChannelControl (manager);

        if(led && !vibration) {
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setLights (Color.RED, 500, 2000)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (2, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (2, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (2, notification3.build ( ));
            }
        }
        else if(vibration && !led){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_3_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setLights (Color.RED, 500, 2000)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_3_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_3_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (3, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (3, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (3, notification3.build ( ));
            }
        }
        else if(vibration && led){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_4_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setLights (Color.RED, 500, 2000)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_4_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_4_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (4, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (4, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (4, notification3.build ( ));
            }
        }
        else{
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_1_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setLights (Color.RED, 500, 2000)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_1_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_1_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setLights (Color.RED, 500, 2000)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (1, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (1, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (1, notification3.build ( ));
            }
        }



    }

    private void notificationChannelControl(NotificationManager manager) {

        if(led) {
            notificationChannel = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights (true);
            notificationChannel.setDescription("Notification Channel");
            if(notificationChannel.getId ( ).equals (CHANNEL_2_ID)) {
                manager.createNotificationChannel (notificationChannel);
            }
        }
        else {
            if(notificationChannel != null) {
                manager.deleteNotificationChannel (CHANNEL_2_ID);
                notificationChannel = null;
            }
        }

        if(vibration){
            notificationChannel = new NotificationChannel (
                    CHANNEL_3_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableVibration (true);
            notificationChannel.setDescription("Notification Channel");
            if(notificationChannel.getId ( ).equals (CHANNEL_3_ID)) {
                manager.createNotificationChannel (notificationChannel);
            }
        }
        else {
            if(notificationChannel != null) {
                manager.deleteNotificationChannel (CHANNEL_3_ID);
                notificationChannel = null;
            }
        }

        if(led && vibration) {
            notificationChannel = new NotificationChannel(
                    CHANNEL_4_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.enableLights (true);
            notificationChannel.enableVibration (true);
            notificationChannel.setDescription("Notification Channel");
            if(notificationChannel.getId ( ).equals (CHANNEL_4_ID)) {
                manager.createNotificationChannel (notificationChannel);
            }
        }
        else {
            if(notificationChannel != null && !vibration && !led) {
                manager.deleteNotificationChannel (CHANNEL_4_ID);
                notificationChannel = null;

            }
        }

    }
}

