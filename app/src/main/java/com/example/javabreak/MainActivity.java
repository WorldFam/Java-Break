package com.example.javabreak;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.firezenk.bubbleemitter.BubbleEmitterView;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView time;
    private TimePicker timePicker;
    private long mSec, storeTime;
    private CountDownTimer countDownTimer;
    private ImageButton reset, pauseResume;
    Animation resetAnimation;

    private Dialog dialog;
    private Button start, continueWork,snooze,takeABreak;
    ProgressBar progressBar;
    TimerState timerState;
    String timeLeftFormatted = "00:00:00";
    BubbleEmitter bubbleEmitter;

    ImageView transparentBackground,transparentBackgroundZoomIn ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerState = TimerState.CREATE;
        updateViews();
        timePicker.setHour(0);
        timePicker.setMinute(15);

        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                timeConversion();
                startTimer();
                updateViews();
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
                if (timerState == TimerState.RUNNING) {
                    pauseTimer();
                } else {
                    timerState = TimerState.RESUME;
                    updateViews();
                    startTimer();
                }
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeConversion();
                countDownTime();
            }
        });
    }


    public void startTimer() {
        timerState = TimerState.RUNNING;
        //bubbleEmitter.runnable.run(); : QUESTIONABLE
        storeTime = mSec;
        countDownTimer = new CountDownTimer(mSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
                mSec = millisUntilFinished;
                countDownTime();
            }
            @Override
            public void onFinish() {
                timerState = TimerState.FINISHED;
                updateViews();
                continueWork = dialog.findViewById(R.id.continueWorking);
                takeABreak = dialog.findViewById(R.id.takeABreak);
                snooze = dialog.findViewById(R.id.snooze);
                takeABreak.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setTakeABreak();
                        dialog.cancel();
                    }
                });

                snooze.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setSnooze();
                        dialog.cancel();
                    }
                });

                continueWork.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mSec = storeTime;
                        startTimer();
                        dialog.cancel();
                    }
                });
            }
        }.start();
    }

    private void resetTimer() {
        timerState = TimerState.RESET;
        updateViews();

    }

    private void pauseTimer() {
        timerState = TimerState.PAUSED;
        updateViews();
    }

    private void setTakeABreak()
    {
        long breakTime = 5000;
        mSec = breakTime;
        startTimer();
    }

    private void setSnooze()
    {
        long snoozeTime = 5000;
        mSec = snoozeTime;
        startTimer();
    }


    private void countDownTime() {
        String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(mSec) % 60,
                TimeUnit.MILLISECONDS.toMinutes(mSec) % 60,
                TimeUnit.MILLISECONDS.toSeconds(mSec) % 60);
        time.setText(text);
    }


    private void setProgressBarValues() {
        progressBar.setMax((int) mSec / 1000);
        progressBar.setProgress((int) mSec / 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void timeConversion()
    {
        int hour = timePicker.getHour();
        long min = timePicker.getMinute() + TimeUnit.HOURS.toMinutes(hour);
        mSec = TimeUnit.MINUTES.toMillis(min);   //int mSec = min * 60000;  Old
    }


    private void updateViews()
    {
        switch (timerState) {
            case CREATE:
                time = findViewById(R.id.time);
                start = findViewById(R.id.button);
                progressBar = findViewById(R.id.progressBar);
                reset = findViewById(R.id.button2);
                reset.setEnabled(false);
                pauseResume = findViewById(R.id.button3);
                pauseResume.setEnabled(false);
                timePicker = findViewById(R.id.simpleTimePicker);
                timePicker.setIs24HourView(true);
                transparentBackground = findViewById(R.id.transparentBackground);
                transparentBackgroundZoomIn = findViewById(R.id.transparentBackgroundZoomIn);
                transparentBackgroundZoomIn.setVisibility(View.INVISIBLE);
                break;
            case RUNNING:
                setProgressBarValues();
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                transparentBackgroundZoomIn.clearAnimation();
                transparentBackgroundZoomIn.setVisibility(View.INVISIBLE);
                break;
            case PAUSED:
                countDownTimer.cancel();
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                break;
            case RESUME:
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case SNOOZE:

                break;
            case BREAK:

                break;
            case FINISHED:
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                time.setText(timeLeftFormatted);
                start.setEnabled(true);
                pauseResume.setEnabled(false);
                reset.setEnabled(false);
                break;
            case RESET:
                Animation animationZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                timePicker.setEnabled(true);
                transparentBackgroundZoomIn.setVisibility(View.VISIBLE);
                transparentBackgroundZoomIn.startAnimation(animationZoomIn);
                progressBar.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
                resetAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                reset.startAnimation(resetAnimation);
                reset.setEnabled(false);
                pauseResume.setEnabled(false);
                start.setEnabled(true);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                time.setText(timeLeftFormatted);
                break;
        }

    }
}