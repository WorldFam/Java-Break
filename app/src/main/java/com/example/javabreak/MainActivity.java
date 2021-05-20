package com.example.javabreak;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.adapters.SectionPageAdapter;
import com.example.javabreak.notifications.AlertReceiver;
import com.example.javabreak.notifications.NotificationReceiver;
import com.example.javabreak.notifications.NotificationService;
import com.example.javabreak.other.NonSwappableViewPager;
import com.example.javabreak.viewmodels.NewReminderFragmentViewModel;
import com.example.javabreak.viewmodels.SettingsFragmentViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity  {
    private final int[] tabIcons = {
            R.drawable.ic_baseline_timer_24,
            R.drawable.ic_baseline_toc_24,
            R.drawable.ic_baseline_settings_24
    };

    TabLayout tabLayout;
    NonSwappableViewPager viewPager;
    NewReminderFragmentViewModel newReminderFragmentViewModel;
    SettingsFragmentViewModel settingsFragmentViewModel;
    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newReminderFragmentViewModel =  new ViewModelProvider (this).get(NewReminderFragmentViewModel.class);
        settingsFragmentViewModel =  new ViewModelProvider (this).get(SettingsFragmentViewModel.class);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionPageAdapter(getSupportFragmentManager()));

        tabLayout =  findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.productive).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setText(R.string.scheduled).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setText(R.string.settings).setIcon(tabIcons[2]);

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
                tab.getIcon().setColorFilter (Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        settingsFragmentViewModel.getLedValue ().observe (this, new Observer<Boolean> ( ) {
            @Override
            public void onChanged(Boolean aBoolean) {
                led (aBoolean);
            }
        });

        settingsFragmentViewModel.getVibrationValue ().observe (this, new Observer<Boolean> ( ) {
            @Override
            public void onChanged(Boolean aBoolean) {
                vibration (aBoolean);
            }
        });

        settingsFragmentViewModel.getSoundValue ().observe (this, new Observer<Integer> ( ) {
            @Override
            public void onChanged(Integer integer) {
                sound (integer);
            }
        });


    }

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

    public void vibration(boolean state){
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra ("vibration", state);
    }

    public void led(boolean state){
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra ("led", state);
    }

    public void sound(int index){
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra ("sound", index);
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
        intent.putExtra ("name", newReminderFragmentViewModel.getName ().getValue ());
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
        intent.putExtra ("name", newReminderFragmentViewModel.getName ().getValue ());
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


    public void cancelAlarm() {
        Intent myIntent = new Intent(getApplicationContext (), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext (), 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager != null) {
            alarmManager.cancel (pendingIntent);
        }
    }

    public void notify(long time,String timerState) {
        long when = System.currentTimeMillis() + time;
        Intent myIntent = new Intent(getApplicationContext (), NotificationReceiver.class);
        myIntent.putExtra ("timerState",timerState);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext (), 0, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, when, pendingIntent);
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


    public void notifyService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        ContextCompat.startForegroundService(this,serviceIntent);
    }
}