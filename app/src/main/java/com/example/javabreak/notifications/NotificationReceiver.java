package com.example.javabreak.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.javabreak.notifications.App.CHANNEL_1_ID;

public class NotificationReceiver extends BroadcastReceiver {
    boolean vibration;
    Vibrator vibrator;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context ,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String key = intent.getStringExtra ("timerState");
        Log.d("STATE",String.valueOf (key) + " Timer State Notification");

        vibration = intent.getBooleanExtra ("vibration",false);
        Log.d("STATE",String.valueOf (vibration) + " Vibration");

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        vibration = sharedPreferences.getBoolean ("vibration",false);

        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        NotificationCompat.Builder notification1 = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle("Work session is done!")
                .setContentText("Time to take a break!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder notification2 = new NotificationCompat.Builder(context , CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle("Break session is over!")
                .setContentText("Continue working! You are doing great Job!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder notification3 = new NotificationCompat.Builder(context , CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle("Snooze session is over!")
                .setContentText("Take a break or continue working")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        if(vibration){
            Log.d("STATE",String.valueOf (vibration) + " Vibration TRUE");
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_DOUBLE_CLICK));
        }
        else {
            Log.d("STATE",String.valueOf (vibration) + " Vibration FALSE");
            vibrator.cancel ();
        }


        if(key.equals ("WORK")){
            manager.notify (1, notification1.build ( ));
        }
        if(key.equals ("BREAK")){
            manager .notify(1, notification2.build ());
        }
        if(key.equals ("SNOOZE")){
            manager .notify(1, notification3.build());
        }


//        notification3.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                .setVibrate(new long[]{0L})
//



//        manager.notify (1, notification1.build ( ));

    }

}
