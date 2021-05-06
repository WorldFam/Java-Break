package com.example.javabreak.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.ConfigurationsFragment;
import com.example.javabreak.R;
import com.example.javabreak.activities.MainActivity;
import com.example.javabreak.activities.TimerState;
import com.example.javabreak.viewmodel.ConfigurationPanelViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class FirstFragment extends Fragment {

    ConfigurationPanelViewModel configurationPanelViewModel;
    private TextView time, messageText;
    private TimePicker timePicker;
    private long leftTime,breakTime,snoozeTime, continueWorkTime ,setTime , setBreakTime, setSnoozeTime;
    private long endTime, endBreakTime, endSnoozeTime, endContinueWorkTime;
    private CountDownTimer countDownTimer;
    private ImageButton reset, pauseResume, configuration;
    Animation resetRotate, resetBlink, slideDownText;
    String timeLeftFormatted = "00:00:00";
    String resetTimeFormatted = "00:15:00";
    private Dialog dialog;
    private Button start, continueWork,snooze,takeABreak;
    ProgressBar progressBar;
    TimerState timerState;
    ImageView  transparentBackground ,breakIcon,snoozeIcon ;
    Toast toast;
    TabLayout tabLayout;
    boolean finished = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurationPanelViewModel = new ViewModelProvider(requireActivity()).get(ConfigurationPanelViewModel.class);
        configurationPanelViewModel.getBreakTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                setBreakTime = TimeUnit.SECONDS.toMillis(integer);
                breakTime = setBreakTime;
            }
        });

        configurationPanelViewModel.getSnoozeTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                setSnoozeTime = TimeUnit.SECONDS.toMillis(integer);
                snoozeTime = setSnoozeTime;
                Log.d("STATE",String.valueOf(setSnoozeTime + " setSnooze " + snoozeTime + " snoozeTime"));

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        tabLayout = getActivity().findViewById(R.id.tabLayout);
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
                    leftTime =
                            10000;
                    setTime = leftTime;
                    continueWorkTime = setTime ;
                    ((MainActivity)getActivity()).notifyAlarm(leftTime);
                    setProgressBarValues(leftTime);
                    timerState = TimerState.START;
                    updateViews();
                    startTimer();
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
                if (timerState == TimerState.WORK) {
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
                ConfigurationsFragment fragment = new ConfigurationsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out);
                fragmentTransaction.add(R.id.firstFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                if(tabLayout != null) {
                    tabLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart( );
        loadData();
    }

    private void loadData() {
//        SharedPreferences preferences = getActivity().getSharedPreferences("workTime", MODE_PRIVATE);
//            preferences.edit().remove("timerState").apply();
//            preferences.edit().remove("endTime").apply();
//            preferences.edit().remove("endBreakTime").apply();
//            preferences.edit().remove("endSnoozeTime").apply();
//            preferences.edit().remove("endContinueWorkTime").apply();
//            preferences.edit().remove("leftTime").apply();
//            preferences.edit().remove("setTime").apply();
//            preferences.edit().remove("setBreakTime").apply();
//            preferences.edit().remove("breakTime").apply();
//            preferences.edit().remove("snoozeTime").apply();
//            preferences.edit().remove("setSnoozeTime").apply();
//            preferences.edit().remove("continueWorkTime").apply();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("workTime", MODE_PRIVATE);
        String timerStateCheck = sharedPreferences.getString("timerState","");

        Log.d("STATE",String.valueOf(timerStateCheck) + " Timer State");

        setTime = sharedPreferences.getLong("setTime", 900000);
        leftTime = sharedPreferences.getLong("leftTime", setTime);
        Log.d("STATE",String.valueOf(leftTime) + " left Time");

        continueWorkTime = sharedPreferences.getLong("continueWorkTime", setTime);

        setBreakTime = sharedPreferences.getLong("setBreakTime", 900000);
        breakTime = sharedPreferences.getLong("breakTime", setBreakTime);

        setSnoozeTime = sharedPreferences.getLong("setSnoozeTime", 60000);
//        Log.d("STATE",String.valueOf(setSnoozeTime) + " snooze Time");
        snoozeTime = sharedPreferences.getLong("snoozeTime", setSnoozeTime);
//        Log.d("STATE",String.valueOf(snoozeTime) + " not set snooze Time");

        countDownTime();

        if(timerStateCheck.equals("PAUSE")) {
            timerState = TimerState.PAUSE;
            setProgressBarValues(setTime);
            progressBar.setProgress((int) (leftTime/ 1000));
            updateViews();
        }

        if(timerStateCheck.equals("RESET"))
        {
            timerState = TimerState.RESET;

        }

        if(timerStateCheck.equals("WORK")) {
            timerState = TimerState.WORK;
            endTime = sharedPreferences.getLong("endTime", 0);
            setProgressBarValues(setTime);
            leftTime = endTime - System.currentTimeMillis();
            Log.d("STATE",String.valueOf(leftTime) + " left Time after");

            progressBar.setProgress((int) (setTime/ 1000) - (int) (leftTime/ 1000));
            reset.setVisibility(View.VISIBLE); //Shouldn't belong here
            updateViews();
            if (leftTime < 0) {
                leftTime = 0; //Does allow Timer to be negative
            }
            if(leftTime != 0) {
                startTimer();
            } else {
                if(dialog != null) {
                    dialog.cancel();
                }
                onFinished();
            }
        }

        if(timerStateCheck.equals("BREAK"))
        {
            timerState = TimerState.BREAK;
            endBreakTime = sharedPreferences.getLong("endBreakTime", 0);
            setProgressBarValues(setBreakTime);
            breakTime = endBreakTime - System.currentTimeMillis();
            progressBar.setProgress((int) (setBreakTime/ 1000) - (int) (breakTime/ 1000));
            updateViews();
            if (breakTime < 0) {
                breakTime = 0; //Does allow Timer to be negative
            }
            if(breakTime != 0) {
                breakTimer();
            }
            else {
                if(dialog != null) {
                    dialog.cancel();
                }
                onFinished();
            }
        }
        if(timerStateCheck.equals("SNOOZE"))
        {
            timerState = TimerState.SNOOZE;
            endSnoozeTime = sharedPreferences.getLong("endSnoozeTime", 0);
            setProgressBarValues(setSnoozeTime);
            snoozeTime = endSnoozeTime - System.currentTimeMillis();
            progressBar.setProgress((int) (setSnoozeTime/ 1000) - (int) (snoozeTime/ 1000));
            updateViews();
            if (snoozeTime < 0) {
                snoozeTime = 0; //Does allow Timer to be negative
            }
            if(snoozeTime != 0) {
                snoozeTimer();
            }
            else {
                if(dialog != null) {
                    dialog.cancel();
                }
                onFinished();
            }
        }
        if(timerStateCheck.equals("CONTINUE"))
        {
            timerState = TimerState.CONTINUE;
            endContinueWorkTime = sharedPreferences.getLong("endContinueWorkTime", 0);
            setProgressBarValues(setTime);
            continueWorkTime = endContinueWorkTime - System.currentTimeMillis();
            progressBar.setProgress((int) (setTime/ 1000) - (int) (continueWorkTime/ 1000));
            updateViews();

            if (continueWorkTime < 0) {
                continueWorkTime = 0; //Does allow Timer to be negative
            }

            if(continueWorkTime != 0) {
                continueWorkTime();
            }

            else {
                if(dialog != null) {
                    dialog.cancel();
                }
                onFinished();
            }
        }
    }

    @Override
    public  void onStop() {
        super.onStop();
        saveData( );
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("workTime", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("setTime",setTime);
        editor.putLong("setBreakTime",setBreakTime);
        editor.putLong("setSnoozeTime",setSnoozeTime);


        editor.putLong("leftTime", leftTime);
        editor.putLong("continueWorkTime",continueWorkTime);
        editor.putLong("breakTime",breakTime);
        editor.putLong("snoozeTime",snoozeTime);


        editor.putLong("endTime",endTime);
        editor.putLong("endBreakTime",endBreakTime);
        editor.putLong("endSnoozeTime",endSnoozeTime);
        editor.putLong("endContinueWorkTime",endContinueWorkTime);

        if(timerState == TimerState.WORK)
        {
            editor.putString("timerState","WORK");
        }
        if(timerState == TimerState.PAUSE)
        {
            editor.putString("timerState","PAUSE");
        }
        if(timerState == TimerState.BREAK)
        {
            editor.putString("timerState","BREAK");
        }
        if(timerState == TimerState.SNOOZE)
        {
            editor.putString("timerState","SNOOZE");
        }
        if(timerState == TimerState.CONTINUE)
        {
            editor.putString("timerState","CONTINUE");
        }
        if(timerState == TimerState.RESET)
        {
            editor.putString("timerState","RESET");
            editor.putLong("leftTime", 900000);
        }
        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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
        endTime = System.currentTimeMillis() + leftTime;
        endBreakTime = System.currentTimeMillis() + breakTime;
        endSnoozeTime = System.currentTimeMillis() + snoozeTime;
        endContinueWorkTime = System.currentTimeMillis() + continueWorkTime;
        countDownTimer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
                leftTime = millisUntilFinished;
                countDownTime();
            }
                @Override
            public void onFinish() {
                    onFinished();
                }
            }.start();
        }


    private void onFinished() {
        timerState = TimerState.FINISH;
        updateViews();
        finished = true;
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
                continueWorkTime();
                setProgressBarValues(continueWorkTime);
                dialog.cancel();
            }
        });
    }

    private void pauseTimer() {
        timerState = TimerState.PAUSE;
        countDownTimer.cancel();
        updateViews();
    }

    private void resumeTimer()
    {
        timerState = TimerState.WORK;
        updateViews();
        startTimer();
    }

    private void breakTimer()
    {
        timerState = TimerState.BREAK;
        updateViews();
        if(finished) {
            breakTime = setBreakTime; //setProgressBar
            leftTime = setBreakTime; //setRealTime
            finished = false;
        } else {
            leftTime = breakTime;
        }
        startTimer();
    }

    private void snoozeTimer()
    {
        timerState = TimerState.SNOOZE;
        updateViews();
        if(finished) {
            snoozeTime = setSnoozeTime; //setProgressBar
            leftTime = setSnoozeTime; //setRealTime
            finished = false;
        } else {
            leftTime = snoozeTime;
        }
        startTimer();
    }

    private void continueWorkTime()
    {
        timerState = TimerState.CONTINUE;
        updateViews();
        if(finished) {
            continueWorkTime = setTime; //setProgressBar
            leftTime = setTime; //setRealTime
            finished = false;
        } else {
            leftTime = continueWorkTime;
        }
        startTimer();
    }

    private void resetTimer() {
        timerState = TimerState.RESET;
        snoozeTime = setSnoozeTime;
        breakTime = setBreakTime;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        updateViews();
    }

    private void countDownTime() {
        String text = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(leftTime) % 60,
                TimeUnit.MILLISECONDS.toMinutes(leftTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(leftTime) % 60);
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
        leftTime = TimeUnit.MINUTES.toMillis(min);   //int mSec = min * 60000;  Old
    }


    private void updateViews()
    {
        switch (timerState) {
            case START:
                slideDownText = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down_text);
                resetBlink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                messageText.startAnimation(slideDownText);
                slideDownText.setAnimationListener(new Animation.AnimationListener( ) {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        reset.setVisibility(View.VISIBLE);
                        reset.startAnimation(resetBlink);
                        messageText.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                timerState = TimerState.WORK;
            case WORK:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case PAUSE:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                break;
            case SNOOZE:
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                reset.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                snoozeIcon.setVisibility(View.VISIBLE);
                pauseResume.setEnabled(false);
                break;
            case BREAK:
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                reset.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.VISIBLE);
                pauseResume.setEnabled(false);
                break;
            case CONTINUE:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                start.setEnabled(false);
                timePicker.setEnabled(false);
                start.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                reset.setVisibility(View.VISIBLE);
                break;
            case FINISH:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_dialog);
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                messageText.setVisibility(View.INVISIBLE);
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
                if(dialog != null) {
                    dialog.cancel( );
                }
                resetRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                reset.startAnimation(resetRotate);
                resetRotate.setAnimationListener(new Animation.AnimationListener( ) {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        messageText.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                reset.setEnabled(false);
                pauseResume.setEnabled(false);
                start.setEnabled(true);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                snoozeIcon.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.INVISIBLE);
                time.setText(resetTimeFormatted);
                break;
        }

    }


}