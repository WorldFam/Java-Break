package com.example.javabreak.notifications;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.javabreak.fragments.ThirdFragment.NO_SOUND;
import static com.example.javabreak.notifications.App.CHANNEL_10_ID;
import static com.example.javabreak.notifications.App.CHANNEL_11_ID;
import static com.example.javabreak.notifications.App.CHANNEL_12_ID;
import static com.example.javabreak.notifications.App.CHANNEL_13_ID;
import static com.example.javabreak.notifications.App.CHANNEL_14_ID;
import static com.example.javabreak.notifications.App.CHANNEL_15_ID;
import static com.example.javabreak.notifications.App.CHANNEL_16_ID;
import static com.example.javabreak.notifications.App.CHANNEL_17_ID;
import static com.example.javabreak.notifications.App.CHANNEL_18_ID;
import static com.example.javabreak.notifications.App.CHANNEL_19_ID;
import static com.example.javabreak.notifications.App.CHANNEL_1_ID;
import static com.example.javabreak.notifications.App.CHANNEL_20_ID;
import static com.example.javabreak.notifications.App.CHANNEL_21_ID;
import static com.example.javabreak.notifications.App.CHANNEL_22_ID;
import static com.example.javabreak.notifications.App.CHANNEL_23_ID;
import static com.example.javabreak.notifications.App.CHANNEL_24_ID;
import static com.example.javabreak.notifications.App.CHANNEL_2_ID;
import static com.example.javabreak.notifications.App.CHANNEL_3_ID;
import static com.example.javabreak.notifications.App.CHANNEL_4_ID;
import static com.example.javabreak.notifications.App.CHANNEL_5_ID;
import static com.example.javabreak.notifications.App.CHANNEL_6_ID;
import static com.example.javabreak.notifications.App.CHANNEL_7_ID;
import static com.example.javabreak.notifications.App.CHANNEL_8_ID;
import static com.example.javabreak.notifications.App.CHANNEL_9_ID;
import static com.example.javabreak.notifications.App.GROUP_1_ID;
import static com.example.javabreak.notifications.App.GROUP_2_ID;
import static com.example.javabreak.notifications.App.GROUP_3_ID;
import static com.example.javabreak.notifications.App.GROUP_4_ID;
import static com.example.javabreak.notifications.App.GROUP_5_ID;

public class NotificationReceiver extends BroadcastReceiver {
    boolean vibration,led;
    int soundIndex;
    Vibrator vibrator;
    NotificationChannel notificationChannel;
    Uri sound1, sound2, sound3, sound4, sound5;
    NotificationChannelGroup notificationChannelGroup;
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
        soundIndex = intent.getIntExtra ("soundIndex",NO_SOUND);

        Log.d ("Sound",String.valueOf (soundIndex) + " CHECK RECEIVER ");

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        vibration = sharedPreferences.getBoolean ("vibration",false);
        led = sharedPreferences.getBoolean ("led",false);
        boolean sound = sharedPreferences.getBoolean ("sound",false);
        if(sound) soundIndex = sharedPreferences.getInt ("soundIndex", NO_SOUND);
        else soundIndex = NO_SOUND;

        Log.d ("Sound",String.valueOf (soundIndex) );

        sound1 = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()+ "/" + R.raw.sound1 ) ;
        sound2 = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()+ "/" + R.raw.sound2 ) ;
        sound3 = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()+ "/" + R.raw.sound3 ) ;
        sound4 = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()+ "/" + R.raw.sound4 ) ;
        sound5 = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()+ "/" + R.raw.sound5 ) ;

        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            notificationChannelControl (manager);
        }

        if(soundIndex == NO_SOUND){
            notificationDefault (context, manager, contentIntent, key);
        }
        else if(soundIndex != NO_SOUND && !led && !vibration){
            notificationOnlySound (context, manager, contentIntent, key);
        }
        else if(soundIndex != NO_SOUND && led && !vibration)
        {
            notificationWithSoundAndLed (context, manager, contentIntent, key);
        }
        else if(soundIndex != NO_SOUND && !led && vibration)
        {
            notificationWithSoundAndVibration (context, manager, contentIntent, key);
        }
        else if(soundIndex != NO_SOUND && led && vibration)
        {
            notificationWithSoundLedAndVibration (context, manager, contentIntent, key);
        }
    }
    private void notificationWithSoundLedAndVibration(Context context, NotificationManager manager, PendingIntent contentIntent, String key) {
        if(soundIndex == 0){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_20_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_20_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_20_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (20, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (20, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (20, notification3.build ( )); }
        }
        if(soundIndex == 2){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_21_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_21_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setPriority (NotificationCompat.PRIORITY_HIGH)
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_21_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (21, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (21, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (21, notification3.build ( )); }
        }
        if(soundIndex == 4){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_22_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_22_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_22_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (22, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (22, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (22, notification3.build ( )); }
        }
        if(soundIndex == 6){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_23_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_23_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_23_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (23, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (23, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (23, notification3.build ( )); }
        }
        if(soundIndex == 8){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_24_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_24_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_24_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (24, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (24, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (24, notification3.build ( )); }
        }
    }

    private void notificationWithSoundAndVibration(Context context, NotificationManager manager, PendingIntent contentIntent, String key) {
        if(soundIndex == 0){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_15_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_15_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_15_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (15, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (15, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (15, notification3.build ( )); }
        }
        if(soundIndex == 2){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_16_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_16_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_16_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (16, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (16, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (16, notification3.build ( )); }
        }
        if(soundIndex == 4){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_17_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_17_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_17_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (17, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (17, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (17, notification3.build ( )); }
        }
        if(soundIndex == 6){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_18_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_18_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_18_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (18, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (18, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (18, notification3.build ( )); }
        }
        if(soundIndex == 8){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_19_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_19_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_19_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (19, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (19, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (19, notification3.build ( )); }
        }
    }

    private void notificationWithSoundAndLed(Context context, NotificationManager manager, PendingIntent contentIntent, String key) {
        if(soundIndex == 0){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_10_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_10_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_10_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (10, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (10, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (10, notification3.build ( )); }
        }
        if(soundIndex == 2){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_11_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_11_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_11_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (11, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (11, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (11, notification3.build ( )); }
        }
        if(soundIndex == 4){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_12_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_12_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_12_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (12, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (12, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (12, notification3.build ( )); }
        }
        if(soundIndex == 6){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_13_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_13_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_13_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (13, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (13, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (13, notification3.build ( )); }
        }
        if(soundIndex == 8){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_14_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_14_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_14_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (14, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (14, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (14, notification3.build ( )); }
        }
    }

    private void notificationDefault(Context context, NotificationManager manager, PendingIntent contentIntent, String key) {
        if(led && !vibration) {
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_2_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
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
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_3_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_3_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
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
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_4_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_4_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
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
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_1_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_1_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
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

    private void notificationOnlySound(Context context, NotificationManager manager, PendingIntent contentIntent, String key) {
        if(soundIndex == 0){
        NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_5_ID)
                .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle ("Work session is done!")
                .setContentText ("Time to take a break!")
                .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                .setColor (Color.BLUE)
                .setContentIntent (contentIntent)
                .setAutoCancel (true);

        NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_5_ID)
                .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle ("Break session is over!")
                .setContentText ("Continue working! You are doing great Job!")
                .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                .setColor (Color.BLUE)
                .setContentIntent (contentIntent)
                .setAutoCancel (true);

        NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_5_ID)
                .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle ("Snooze session is over!")
                .setContentText ("Take a break or continue working")
                .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                .setColor (Color.BLUE)
                .setContentIntent (contentIntent)
                .setAutoCancel (true);

        if (key.equals ("WORK")) {
            manager.notify (5, notification1.build ( ));
        }
        if (key.equals ("BREAK")) {
            manager.notify (5, notification2.build ( ));
        }
        if (key.equals ("SNOOZE")) {
            manager.notify (5, notification3.build ( )); }
        }
        if(soundIndex == 2){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_6_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_6_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_6_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (6, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (6, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (6, notification3.build ( )); }
        }
        if(soundIndex == 4){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_7_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_7_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_7_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (7, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (7, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (7, notification3.build ( )); }
        }
        if(soundIndex == 6){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_8_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_8_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_8_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (8, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (8, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (8, notification3.build ( )); }
        }
        if(soundIndex == 8){
            NotificationCompat.Builder notification1 = new NotificationCompat.Builder (context, CHANNEL_9_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Work session is done!")
                    .setContentText ("Time to take a break!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification2 = new NotificationCompat.Builder (context, CHANNEL_9_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Break session is over!")
                    .setContentText ("Continue working! You are doing great Job!")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            NotificationCompat.Builder notification3 = new NotificationCompat.Builder (context, CHANNEL_9_ID)
                    .setSmallIcon (R.drawable.ic_baseline_add_alarm_24)
                    .setContentTitle ("Snooze session is over!")
                    .setContentText ("Take a break or continue working")
                    .setCategory (NotificationCompat.CATEGORY_MESSAGE)
                    .setColor (Color.BLUE)
                    .setContentIntent (contentIntent)
                    .setAutoCancel (true);

            if (key.equals ("WORK")) {
                manager.notify (9, notification1.build ( ));
            }
            if (key.equals ("BREAK")) {
                manager.notify (9, notification2.build ( ));
            }
            if (key.equals ("SNOOZE")) {
                manager.notify (9, notification3.build ( )); }
        }
    }

    private void notificationChannelControl(NotificationManager manager) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                .setUsage(AudioAttributes. USAGE_NOTIFICATION )
                .build() ;

        if(soundIndex == NO_SOUND) {
            noSound (manager);
        }
        else if(soundIndex != NO_SOUND && !led && !vibration){
            onlyWithSound (manager, audioAttributes);
        }
        else if(soundIndex != NO_SOUND && led && !vibration){
            soundWithLed (manager, audioAttributes);
        }
        else if(soundIndex != NO_SOUND && !led && vibration){
            soundWithVibration (manager, audioAttributes);
        }
        else if(soundIndex != NO_SOUND && led && vibration){
            soundWithLedAndVibration (manager, audioAttributes);
        }
    }

    private void soundWithLedAndVibration(NotificationManager manager, AudioAttributes audioAttributes) {
        notificationChannelGroup = new NotificationChannelGroup(
                GROUP_5_ID,
                "Group"
        );
        manager.createNotificationChannelGroup (notificationChannelGroup);

        if (soundIndex == 0) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_20_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_5_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound1, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_20_ID);
        }
        if (soundIndex == 2) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_21_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_5_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound2, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_21_ID);
        }
        if (soundIndex == 4) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_22_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_5_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound3, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_22_ID);
        }
        if (soundIndex == 6) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_23_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_5_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound4, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_23_ID);
        }
        if (soundIndex == 8) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_24_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_5_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound5, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_24_ID);
        }
        manager.deleteNotificationChannelGroup (GROUP_1_ID);
        manager.deleteNotificationChannelGroup (GROUP_2_ID);
        manager.deleteNotificationChannelGroup (GROUP_3_ID);
        manager.deleteNotificationChannelGroup (GROUP_4_ID);
    }

    private void soundWithVibration(NotificationManager manager, AudioAttributes audioAttributes) {
        notificationChannelGroup = new NotificationChannelGroup(
                GROUP_4_ID,
                "Group"
        );
        manager.createNotificationChannelGroup (notificationChannelGroup);

        if (soundIndex == 0) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_15_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_4_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setSound (sound1, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);

        } else {
            manager.deleteNotificationChannel (CHANNEL_15_ID);
        }
        if (soundIndex == 2) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_16_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_4_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setSound (sound2, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);

        } else {
            manager.deleteNotificationChannel (CHANNEL_16_ID);
        }
        if (soundIndex == 4) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_17_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_4_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setSound (sound3, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_17_ID);
        }
        if (soundIndex == 6) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_18_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_4_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setSound (sound4, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);

        } else {
            manager.deleteNotificationChannel (CHANNEL_18_ID);
        }
        if (soundIndex == 8) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_19_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_4_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setSound (sound5, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);

        } else {
            manager.deleteNotificationChannel (CHANNEL_19_ID);
        }
        manager.deleteNotificationChannelGroup (GROUP_1_ID);
        manager.deleteNotificationChannelGroup (GROUP_2_ID);
        manager.deleteNotificationChannelGroup (GROUP_3_ID);
        manager.deleteNotificationChannelGroup (GROUP_5_ID);
    }

    private void soundWithLed(NotificationManager manager, AudioAttributes audioAttributes) {
        notificationChannelGroup = new NotificationChannelGroup(
                GROUP_3_ID,
                "Group"
        );
        manager.createNotificationChannelGroup (notificationChannelGroup);

        if (soundIndex == 0) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_10_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_3_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound1, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_10_ID);
        }
        if (soundIndex == 2) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_11_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_3_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound2, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);

        } else {
            manager.deleteNotificationChannel (CHANNEL_11_ID);

        }
        if (soundIndex == 4) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_12_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_3_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound3, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_12_ID);
        }
        if (soundIndex == 6) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_13_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_3_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound4, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_13_ID);
        }
        if (soundIndex == 8) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_14_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_3_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setSound (sound5, audioAttributes);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_14_ID);
        }
        manager.deleteNotificationChannelGroup (GROUP_1_ID);
        manager.deleteNotificationChannelGroup (GROUP_2_ID);
        manager.deleteNotificationChannelGroup (GROUP_4_ID);
        manager.deleteNotificationChannelGroup (GROUP_5_ID);
    }

    private void onlyWithSound(NotificationManager manager, AudioAttributes audioAttributes) {
        notificationChannelGroup = new NotificationChannelGroup(
                GROUP_2_ID,
                "Group"
        );
        manager.createNotificationChannelGroup (notificationChannelGroup);

        if(soundIndex == 0){
            notificationChannel = new NotificationChannel (
                    CHANNEL_5_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_2_ID);
            notificationChannel.setSound(sound1, audioAttributes) ;
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_5_ID);
        }
        if(soundIndex == 2){
            notificationChannel = new NotificationChannel (
                    CHANNEL_6_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_2_ID);
            notificationChannel.setSound(sound2 , audioAttributes) ;
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_6_ID);
        }
        if(soundIndex == 4){
            notificationChannel = new NotificationChannel (
                    CHANNEL_7_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_2_ID);
            notificationChannel.setSound(sound3, audioAttributes) ;
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_7_ID);
        }
        if(soundIndex == 6){
            notificationChannel = new NotificationChannel (
                    CHANNEL_8_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_2_ID);
            notificationChannel.setSound(sound4, audioAttributes) ;
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
                manager.deleteNotificationChannel (CHANNEL_8_ID);
        }
        if(soundIndex == 8){
            notificationChannel = new NotificationChannel (
                    CHANNEL_9_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_2_ID);
            notificationChannel.setSound(sound5, audioAttributes) ;
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
                manager.deleteNotificationChannel (CHANNEL_9_ID);
            }
        manager.deleteNotificationChannelGroup (GROUP_1_ID);
        manager.deleteNotificationChannelGroup (GROUP_3_ID);
        manager.deleteNotificationChannelGroup (GROUP_4_ID);
        manager.deleteNotificationChannelGroup (GROUP_5_ID);
    }

    private void noSound(NotificationManager manager) {
        notificationChannelGroup = new NotificationChannelGroup(
                GROUP_1_ID,
                "Group"
        );
        manager.createNotificationChannelGroup (notificationChannelGroup);

        if (led) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_2_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_1_ID);
            notificationChannel.enableLights (true);
            notificationChannel.setLightColor (0xffa500);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_2_ID);
        }

        if (vibration) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_3_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_1_ID);
            notificationChannel.enableVibration (true);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_3_ID);
        }

        if (led && vibration) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_4_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_1_ID);
            notificationChannel.enableLights (true);
            notificationChannel.enableVibration (true);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_4_ID);
        }

        if (!led && !vibration) {
            notificationChannel = new NotificationChannel (
                    CHANNEL_1_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setGroup (GROUP_1_ID);
            notificationChannel.setDescription ("Notification Channel");
            manager.createNotificationChannel (notificationChannel);
        } else {
            manager.deleteNotificationChannel (CHANNEL_1_ID);
        }
        manager.deleteNotificationChannelGroup (GROUP_2_ID);
        manager.deleteNotificationChannelGroup (GROUP_3_ID);
        manager.deleteNotificationChannelGroup (GROUP_4_ID);
        manager.deleteNotificationChannelGroup (GROUP_5_ID);
        }
    }


