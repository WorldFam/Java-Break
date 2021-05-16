package com.example.javabreak.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.R;
import com.example.javabreak.adapters.NonSwipeableViewPager;
import com.example.javabreak.adapters.SectionPageAdapter;
import com.example.javabreak.notifications.AlertReceiver;
import com.example.javabreak.notifications.NotificationReceiver;
import com.example.javabreak.viewmodel.ScheduledSecondSharedViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity  {
    private int[] tabIcons = {
            R.drawable.ic_baseline_timer_24,
            R.drawable.ic_baseline_toc_24,
            R.drawable.ic_baseline_settings_24
    };

    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    public ScheduledSecondSharedViewModel scheduledSecondSharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduledSecondSharedViewModel =  new ViewModelProvider (this).get(ScheduledSecondSharedViewModel.class);

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



    public void startAlarm(int dayOfWeek, int hour, int minute, int breakFrequency, int breakDuration) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long breakFrequencyMillis = TimeUnit.MINUTES.toMillis (breakFrequency);
        breakFrequencyAlarm(calendar,breakFrequencyMillis);

        //TODO BREAK DURATION
//        long breakDurationMillis = TimeUnit.SECONDS.toMillis (breakDuration);
//        breakDurationAlarm (calendar,breakDurationMillis);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra ("name",scheduledSecondSharedViewModel.getName ().getValue ());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Check if it not set in the past which would fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (),AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

    public void breakFrequencyAlarm(Calendar calendar,long breakFrequency) {
        long breakFrequencyStart = calendar.getTimeInMillis ()+ breakFrequency;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra ("name",scheduledSecondSharedViewModel.getName ().getValue ());
        intent.putExtra ("text","Time to take a break!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0);

        // Check if it not set in the past which would fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
//        alarmManager.setExact (AlarmManager.RTC_WAKEUP,breakFrequencyStart,pendingIntent);
        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, breakFrequencyStart ,breakFrequency, pendingIntent);
    }


    public void breakDurationAlarm(Calendar calendar, long breakDuration) {
        long breakDurationStart = System.currentTimeMillis () + breakDuration;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra ("name","BREAK DURATION LOOKUP");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 15, intent,0);

        // Check if it not set in the past which would fire instantly
        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, breakDurationStart,breakDuration, pendingIntent);

    }




//    public void endAlarm(int dayOfWeek, int hour, int minute) {
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, 0);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dayOfWeek, intent, 0);
//
//        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 7);
//        }
//        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (),AlarmManager.INTERVAL_DAY * 7, pendingIntent);
//
//    }

//    public void startAlarmTuesday(Calendar calendar) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext (), 2, intent, 0);
//        if (calendar.before(Calendar.getInstance())) {
//            calendar.add(Calendar.DATE, 1);
//        }
//        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 168*60 * 60 * 1000, pendingIntent);
//
//    }
//
//    public void startAlarmWednesday(Calendar calendar) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext (), 3, intent, 0);
//        if (calendar.before(Calendar.getInstance())) {
//            calendar.add(Calendar.DATE, 1);
//        }
//        alarmManager.setRepeating (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 168*60 * 60 * 1000, pendingIntent);
//
//    }

    public void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void notify(long time,String timerState) {

        long when = System.currentTimeMillis() + time;
        Intent myIntent = new Intent(getApplicationContext (), NotificationReceiver.class);
        myIntent.putExtra ("timerState",timerState);
        sendBroadcast(myIntent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext (), 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, when, pendingIntent);
//        Intent myIntent = new Intent(getApplicationContext (), NotificationReceiver.class);
//        myIntent.putExtra ("timerState",timerState);
//        sendBroadcast(myIntent);
    }


    public void notifyAlarm(long time, String timerState) {
        Intent myIntent = new Intent(getApplicationContext (), NotificationReceiver.class);
        myIntent.putExtra ("timerState",timerState);
        myIntent.putExtra ("time",time);
        sendBroadcast(myIntent);
    }

//    public void cancelAlarm()
//    {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent myIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this, 1, myIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        alarmManager.cancel(pendingIntent);
//    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED) || action.equals(Intent.ACTION_DATE_CHANGED))  {
//               cancelAlarm (1);
//               startAlarm(1, 9, 0, 15 , 5 );
            }
        }
    };



    public void lockViewPager() {
        viewPager.setPagingEnabled(false);
    }

    public void openViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager( );
        fragmentManager.popBackStack();
        viewPager.setPagingEnabled(true);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
            openViewPager();
    }
}