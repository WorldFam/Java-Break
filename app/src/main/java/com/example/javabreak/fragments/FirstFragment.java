package com.example.javabreak.fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.ConfigurationActivity;
import com.example.javabreak.R;
import com.example.javabreak.activities.TimerState;
import com.example.javabreak.viewmodel.SecondFragmentViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class FirstFragment extends Fragment {

    SecondFragmentViewModel secondFragmentViewModel;
    private TextView time, messageText;
    private TimePicker timePicker;
    private long mSec,breakTime,snoozeTime, continueWorkTime;
    private CountDownTimer countDownTimer;
    private ImageButton reset, pauseResume, configuration;
    Animation resetRotate, resetBlink, slideDownText;

    private Dialog dialog;
    private Button start, continueWork,snooze,takeABreak;
    ProgressBar progressBar;
    TimerState timerState;
    String timeLeftFormatted = "00:00:00";
    ImageView  transparentBackground ,breakIcon,snoozeIcon ;
    Toast toast;

    Handler handler = new Handler();
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
        defineWidgets(view);

        start.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(timePicker.getMinute() == 0 && timePicker.getHour() == 0)
                {
                    if(toast != null) {
                        toast.cancel();
                    }
                    toast = new Toast(getActivity());
                    toast = Toasty.error(getActivity(), "Can't be less than 1 minute", Toast.LENGTH_SHORT, true);
                    toast.setGravity(Gravity.CENTER, 0, 400);
                    toast.show();
                }
                else {
                    if(toast != null) {
                        toast.cancel();
                    }
                    timeConversion();
                    continueWorkTime = mSec;
/*
                        mSec = 2000;
*/
                    setProgressBarValues(mSec);
                    startTimer();
                    updateViews();
                }
            }
        }
        );


         reset.setOnClickListener(new View.OnClickListener() {
        @Override
     public void onClick(View v) {
                if(toast != null) {
                 toast.cancel();
                }
                toast = new Toast(getActivity());
                toast = Toasty.warning(getActivity(), "Hold to Reset!", Toast.LENGTH_SHORT, true);
                toast.setGravity(Gravity.CENTER, 0, 400);
                toast.show();
        }
          });


        reset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(toast != null) {
                    toast.cancel();
                }
                resetTimer();
                return true;
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

        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConfigurationActivity.class);
                startActivity(intent);
                CustomIntent.customType(getActivity(), "fadein-to-fadeout");
            }
        });


        return view;
    }

    private void defineWidgets(View view) {
        time = view.findViewById(R.id.time);
        start = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        reset = view.findViewById(R.id.button2);
        pauseResume = view.findViewById(R.id.button3);
        timePicker = view.findViewById(R.id.simpleTimePicker);
        transparentBackground = view.findViewById(R.id.transparentBackground);
        breakIcon = view.findViewById(R.id.breakIcon);
        snoozeIcon = view.findViewById(R.id.snoozeIcon);
        configuration = view.findViewById(R.id.configurationButton);
        messageText = view.findViewById(R.id.messageText);

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
                        setProgressBarValues(breakTime);
                        dialog.cancel();
                    }
                });

                snooze.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        snoozeTimer();
                        setProgressBarValues(snoozeTime);
                        dialog.cancel();
                    }
                });

                continueWork.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    /*    count++;
                        if(count == 4)
                        {
                            count = 0;
                        }*/ //MIGHT ADD LONG BRAKE DUNNO YET
                        continueWorkTime();
                        setProgressBarValues(continueWorkTime);
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

    private void resumeTimer()
    {
        timerState = TimerState.RESUME;
        updateViews();
        startTimer();
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

    private void continueWorkTime()
    {
        timerState = TimerState.CONTINUE;
        updateViews();
        startTimer();
    }

    private void resetTimer() {
        timerState = TimerState.RESET;
        updateViews();
    }

    private void countDownTime() {
        String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(mSec) % 60,
                TimeUnit.MILLISECONDS.toMinutes(mSec) % 60,
                TimeUnit.MILLISECONDS.toSeconds(mSec) % 60);
        time.setText(text);
    }

    private void setProgressBarValues(long time) {
        progressBar.setMax((int) time / 1000);
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
                slideDownText = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down_text);
                messageText.startAnimation(slideDownText);
                messageText.setVisibility(View.INVISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetBlink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                        reset.startAnimation(resetBlink);
                        reset.setVisibility(View.VISIBLE);
                    }
                }, 700);
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
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
            case CONTINUE:
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case FINISHED:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                time.setText(timeLeftFormatted);
                break;
            case RESET:
                timePicker.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                pauseResume.setVisibility(View.INVISIBLE);
                pauseResume.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.VISIBLE );
                reset.setVisibility(View.INVISIBLE);
                start.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                countDownTimer.cancel();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(messageText, View.ALPHA, 0f, 1f);
                        fadeIn.start();
                        messageText.setVisibility(View.VISIBLE);
                    }
                }, 700);
                resetRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                reset.startAnimation(resetRotate);
                reset.setEnabled(false);
                pauseResume.setEnabled(false);
                start.setEnabled(true);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                snoozeIcon.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.INVISIBLE);
                time.setText(timeLeftFormatted);
                break;
        }

    }
}