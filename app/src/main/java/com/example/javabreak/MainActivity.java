package com.example.javabreak;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView time;
    private TimePicker timePicker;
    private boolean timeRunning = true;
    private long mSec;
    private CountDownTimer countDownTimer;
    private Button start, pauseResume, reset;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = findViewById(R.id.time);
        start = findViewById(R.id.button);
        reset = findViewById(R.id.button2);
        pauseResume = findViewById(R.id.button3);
        timePicker = findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(0);
        timePicker.setMinute(30);

        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int hour = timePicker.getHour();
                long min = timePicker.getMinute() + TimeUnit.HOURS.toMinutes(hour);
                //int mSec = min * 60000;  Old
                mSec = TimeUnit.MINUTES.toMillis(min);
                startTimer();
            }
        }
        );

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        pauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeRunning) {
                    pauseTimer();
                } else {
                   startTimer();
                }
            }
        });
        countDownTime();

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSec = millisUntilFinished;
                countDownTime();
            }
            @Override
            public void onFinish() {
                time.setText("done!");
            }
        }.start();

        timeRunning = true;
        pauseResume.setText("Pause");
    }

    private void resetTimer() {
        countDownTimer.cancel();
        String timeLeftFormatted = "00:00:00";
        time.setText(timeLeftFormatted);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timeRunning = false;
        pauseResume.setText("Resume");
    }

    private void countDownTime() {
        String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(mSec) % 60,
                TimeUnit.MILLISECONDS.toMinutes(mSec) % 60,
                TimeUnit.MILLISECONDS.toSeconds(mSec) % 60);
        time.setText(text);
    }
}