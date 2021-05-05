package com.example.javabreak.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;

import com.example.javabreak.R;
import com.example.javabreak.adapters.NonSwipeableViewPager;
import com.example.javabreak.adapters.SectionPageAdapter;
import com.example.javabreak.notifications.NotificationReceiver;
import com.google.android.material.tabs.TabLayout;

import static com.example.javabreak.notifications.App.CHANNEL_1_ID;
import static com.example.javabreak.notifications.App.CHANNEL_2_ID;


public class MainActivity extends AppCompatActivity  {
    private int[] tabIcons = {
            R.drawable.ic_baseline_timer_24,
            R.drawable.ic_baseline_toc_24,
            R.drawable.ic_baseline_settings_24
    };
    private NotificationManagerCompat notificationManagerCompat;

    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager()));

        tabLayout =  findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Productive").setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setText("Scheduled").setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setText("Settings").setIcon(tabIcons[2]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);


            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
            }


                @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }

    public void notifyAlarm(long time) {
        long when = System.currentTimeMillis() + time;
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("myAction", "mDoNotify");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }

//
//    public void startService() {
//        String input = "LOL";
//        Intent serviceIntent = new Intent(this, NotificationService.class);
//        serviceIntent.putExtra("inputExtra", input);
//        ContextCompat.startForegroundService(this, serviceIntent);
//    }
//    public void stopService(View v) {
//        Intent serviceIntent = new Intent(this, NotificationService.class);
//        stopService(serviceIntent);
//    }

    public void sendOnChannel1(View v) {
        String title = "TITLE";
        String message = "MESSAGE";

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                 .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManagerCompat.notify(1, notification);
    }
    public void sendOnChannel2(View v) {
        String title = "TITLE";
        String message = "MESSAGE";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2, notification);
    }

    public void lockViewPager() {
        viewPager.setPagingEnabled(false);
    }

    public void openViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager( );
        fragmentManager.popBackStack( );
        viewPager.setPagingEnabled(true);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
            openViewPager();
    }
}