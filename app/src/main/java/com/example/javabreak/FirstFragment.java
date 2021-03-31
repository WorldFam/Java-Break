package com.example.javabreak;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.viewmodel.SecondFragmentViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FirstFragment extends Fragment {

    SecondFragmentViewModel secondFragmentViewModel;
    private TextView time;
    private TimePicker timePicker;
    private long mSec,breakTime,snoozeTime, continueWorkTime;
    private CountDownTimer countDownTimer;
    private ImageButton reset, pauseResume;
    Animation resetAnimation;

    private Dialog dialog;
    private Button start, continueWork,snooze,takeABreak;
    ProgressBar progressBar;
    TimerState timerState;
    String timeLeftFormatted = "00:00:00";

    ImageView transparentBackgroundZoomIn, transparentBackground ,breakIcon,snoozeIcon ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secondFragmentViewModel =  new ViewModelProvider(requireActivity()).get(SecondFragmentViewModel.class);

        secondFragmentViewModel.getBreakTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                breakTime = TimeUnit.MINUTES.toMillis(integer);
            }
        });

        secondFragmentViewModel.getSnoozeTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                snoozeTime = TimeUnit.MINUTES.toMillis(integer);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        time = view.findViewById(R.id.time);
        start = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        reset = view.findViewById(R.id.button2);
        pauseResume = view.findViewById(R.id.button3);
        timePicker = view.findViewById(R.id.simpleTimePicker);
        transparentBackground = view.findViewById(R.id.transparentBackground);
        breakIcon = view.findViewById(R.id.breakIcon);
        snoozeIcon = view.findViewById(R.id.snoozeIcon);
  /*      transparentBackgroundZoomIn = view.findViewById(R.id.transparentBackgroundZoomIn);

        transparentBackgroundZoomIn.setVisibility(View.INVISIBLE);*/
        breakIcon.setVisibility(View.INVISIBLE);
        snoozeIcon.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        pauseResume.setVisibility(View.INVISIBLE);
        pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
        timePicker.setIs24HourView(true);
        pauseResume.setEnabled(false);
        reset.setEnabled(false);
        timePicker.setHour(0);
        timePicker.setMinute(15);

        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                timeConversion();
                continueWorkTime = mSec;
                mSec = 2000;
                setProgressBarValues();
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
                  resumeTimer();
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
        return view;
    }

    public void startTimer() {
        if (timerState == TimerState.BREAK) {
            mSec = breakTime;
            timerState = TimerState.RUNNING;
            startTimer();
        } else if (timerState == TimerState.SNOOZE) {
            mSec = snoozeTime;
            timerState = TimerState.RUNNING;
            startTimer();
        }
        else if (timerState == TimerState.CONTINUE) {
            mSec = continueWorkTime;
            timerState = TimerState.RUNNING;
            startTimer();
        }
        else {
            timerState = TimerState.RUNNING;
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
                        breakTimer();
                        dialog.cancel();
                    }
                });

                snooze.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        snoozeTimer();
                        dialog.cancel();
                    }
                });

                continueWork.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        continueWorkTime();
                        dialog.cancel();
                    }
                });
            }
        }.start();
        }

        }


    private void pauseTimer() {
        timerState = TimerState.PAUSED;
        updateViews();
    }

    private void breakTimer()
    {
        timerState = TimerState.BREAK;
        updateViews();
        startTimer();
    }

    private void snoozeTimer()
    {
        timerState = TimerState.SNOOZE;
        updateViews();
        startTimer();
    }

    private void resumeTimer()
    {
        timerState = TimerState.RESUME;
        updateViews();
        startTimer();
    }

    private void resetTimer() {
        timerState = TimerState.RESET;
        updateViews();
    }

    private void continueWorkTime()
    {
        timerState = TimerState.CONTINUE;
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
            case RUNNING:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
               /* transparentBackgroundZoomIn.clearAnimation();
                transparentBackgroundZoomIn.setVisibility(View.INVISIBLE);*/
                break;
            case PAUSED:
                countDownTimer.cancel();
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                break;
            case RESUME:
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case SNOOZE:
                snoozeIcon.setVisibility(View.VISIBLE);
                break;
            case BREAK:
                breakIcon.setVisibility(View.VISIBLE);
                break;
            case FINISHED:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                pauseResume.setVisibility(View.INVISIBLE);

                time.setText(timeLeftFormatted);
                start.setEnabled(true);
                pauseResume.setEnabled(false);
                reset.setEnabled(false);
                break;
            case RESET:
                Animation animationZoomIn = AnimationUtils.loadAnimation(getContext(),R.anim.zoom_in);
                timePicker.setEnabled(true);
               /* transparentBackgroundZoomIn.setVisibility(View.VISIBLE);
                transparentBackgroundZoomIn.startAnimation(animationZoomIn);*/
                progressBar.setVisibility(View.INVISIBLE);
                pauseResume.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.VISIBLE );
                reset.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
                resetAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
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