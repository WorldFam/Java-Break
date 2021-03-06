package com.example.javabreak.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;
import com.example.javabreak.viewmodels.ConfigurationFragmentViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class FirstFragment extends Fragment {

    ConfigurationFragmentViewModel configurationFragmentViewModel;
    private TextView time, messageText;
    private TimePicker timePicker;
    private long leftTime,breakTime,snoozeTime, continueWorkTime ,setTime , setBreakTime, setSnoozeTime, startTime;
    private long endTime, endBreakTime, endSnoozeTime, endContinueWorkTime,endStartTime;
    private CountDownTimer countDownTimer;
    private ImageButton reset, pauseResume, configuration;
    Animation resetRotate, resetBlink, slideDownText;
    private Dialog dialog;
    private Button startButton;
    private Button cancelTimerButton;
    ProgressBar progressBar;
    TimerState timerState;
    ImageView  transparentBackground ,breakIcon,snoozeIcon ;
    Toast toast;
    TabLayout tabLayout;
    boolean finishedWork, finishedBreak = false;
    boolean autoStartBreakValue,autoStartWorkValue;
    int sessionCounterWork,sessionCounterBreak, dayOfMonth, sessionCounterDay;

    public enum TimerState {
        WORK, PAUSE, SNOOZE, BREAK, CONTINUE, FINISH_WORK, START_WORK, START, RESET
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurationFragmentViewModel = new ViewModelProvider(requireActivity()).get(ConfigurationFragmentViewModel.class);
        configurationFragmentViewModel.getBreakTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {
                setBreakTime = TimeUnit.MINUTES.toMillis(integer);
                breakTime = setBreakTime;
            }
        });

        configurationFragmentViewModel.getSnoozeTime().observe(this, new Observer<Integer>() {

            @Override
            public void onChanged(Integer integer) {

                setSnoozeTime = TimeUnit.MINUTES.toMillis(integer);
                snoozeTime = setSnoozeTime;

            }
        });
        configurationFragmentViewModel.getAutoStartBreakValue ().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean value) {
                autoStartBreakValue = value;
            }
        });

        configurationFragmentViewModel.getAutoStartWorkValue ().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean value) {
                autoStartWorkValue = value;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        tabLayout = getActivity ( ).findViewById(R.id.tabLayout);
        defineWidgets(view);

        startButton.setOnClickListener(new View.OnClickListener() {
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
                                                   setTime = leftTime;
                                                   continueWorkTime = setTime ;
                                                   startTime = setTime ;
                                                   ((MainActivity)getActivity()).notify (leftTime,String.valueOf (TimerState.WORK));
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
                if (timerState == TimerState.WORK || timerState == TimerState.CONTINUE ) {
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
                ConfigurationFragment fragment = new ConfigurationFragment ();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                        R.anim.slide_out);
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
        sessionCountReset ();
    }

    @Override
    public void onResume() {
        super.onResume ( );
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("timePicker", MODE_PRIVATE);
        int hour = sharedPreferences.getInt ("hour",0);
        int minute = sharedPreferences.getInt ("minute",15);
        timePicker.setHour (hour);
        timePicker.setMinute (minute);
    }

    @Override
    public void onPause() {
        super.onPause ( );
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("timePicker", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt ("hour",timePicker.getHour ());
        editor.putInt("minute",timePicker.getMinute ());
        editor.apply ();
    }

    //Reset counter if day changed
    private void sessionCountReset()
    {
        Calendar cal = Calendar.getInstance();
        sessionCounterDay = dayOfMonth;
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        if(dayOfMonth != sessionCounterDay) {
            sessionCounterDay = dayOfMonth;
            sessionCounterWork = 0;
            sessionCounterBreak = 0;
            configurationFragmentViewModel.setSessionCounterWork (sessionCounterWork);
            configurationFragmentViewModel.setSessionCounterBreak (sessionCounterBreak);
        }
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("workTime", MODE_PRIVATE);
        String timerStateCheck = sharedPreferences.getString("timerState","");

        Log.d("STATE",timerStateCheck + " Timer State");

        //SharedPreference for Counter
        sessionCounterWork = sharedPreferences.getInt ("sessionCounter", 0);
        sessionCounterBreak = sharedPreferences.getInt ("sessionCounterBreak", 0);
        sessionCounterDay = sharedPreferences.getInt ("sessionCounterDay", 0);
        dayOfMonth = sharedPreferences.getInt ("dayOfMonth", 0);

        //SharedPreference to check if brake is over
        finishedBreak = sharedPreferences.getBoolean("startWork", false);

        //SharedPreference for total selected time from time picker
        setTime = sharedPreferences.getLong("setTime", 900000);
        //SharedPreference for main time
        leftTime = sharedPreferences.getLong("leftTime", setTime);
        //SharedPreference for work time
        startTime = sharedPreferences.getLong("startTime", setTime);
        continueWorkTime = sharedPreferences.getLong("continueWorkTime", setTime);

        //SharedPreference getting time from view model and set total break time
        setBreakTime = sharedPreferences.getLong("setBreakTime", 900000);
        breakTime = sharedPreferences.getLong("breakTime", setBreakTime);

        //SharedPreference getting time from view model and set total snooze time
        setSnoozeTime = sharedPreferences.getLong("setSnoozeTime", 60000);
        snoozeTime = sharedPreferences.getLong("snoozeTime", setSnoozeTime);

        //SharedPreference for state from ViewModel to check if auto-start is enabled
        SharedPreferences configurations = getActivity().getSharedPreferences("configurations", MODE_PRIVATE);
        autoStartBreakValue = configurations.getBoolean("autoStartBreakValue", false);
        autoStartWorkValue = configurations.getBoolean("autoStartWorkValue", false);

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

            endStartTime  = sharedPreferences.getLong("endStartTime", 0);
            startTime = endStartTime - System.currentTimeMillis ();

            setProgressBarValues(setTime);
            leftTime = endTime - System.currentTimeMillis();
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
                    if (autoStartBreakValue) {
                    breakTimer ( );
                } else {
                        onFinishedWork ( ); }

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
                    if (autoStartWorkValue) {
                            workTimer ( );
                    } else {
                        onFinishedBreak( );

                }
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
                onFinishedWork ();
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
                onFinishedWork ();
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
        editor.putLong("endStartTime",endStartTime);

        editor.putBoolean("startWork", finishedBreak);
        editor.putLong ("startTime",startTime);

        editor.putInt ("sessionCounter", sessionCounterWork);
        editor.putInt ("sessionCounterBreak", sessionCounterBreak);
        editor.putInt("sessionCounterDay",sessionCounterDay);
        editor.putInt("dayOfMonth",dayOfMonth);

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
        startButton = view.findViewById(R.id.button);
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
        configuration.setBackgroundColor(Color.TRANSPARENT);
    }

    public void startTimer() {
        //After starting timer END's of Times sums current time with running time which later is used
        // to display time running, even when application was closed.
        endTime = System.currentTimeMillis() + leftTime;
        endBreakTime = System.currentTimeMillis() + breakTime;
        endSnoozeTime = System.currentTimeMillis() + snoozeTime;
        endContinueWorkTime = System.currentTimeMillis() + continueWorkTime;
        endStartTime = System.currentTimeMillis() + startTime;
        countDownTimer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
                leftTime = millisUntilFinished;
                countDownTime();
            }
            @Override
            public void onFinish() {
                //Check state to see which dialog should be launched
                if (finishedBreak) {
                    if (autoStartWorkValue) {
                        workTimer ();
                        setProgressBarValues(leftTime);
                        updateSessionCounterBreak();
                        finishedWork = true;
                    } else {
                        onFinishedBreak ( );
                    }
                }
                else {
                    if (autoStartBreakValue) {
                        breakTimer ();
                        setProgressBarValues(breakTime);
                        updateSessionCounterWork ();
                        finishedBreak = true;
                    } else {
                        onFinishedWork ( );
                    }
                }
            }
        }.start();
    }

    private void onFinishedBreak(){
        timerState = TimerState.START_WORK;
        updateViews();
        updateSessionCounterBreak();
        finishedBreak = true;
        Button startWorkButton = dialog.findViewById (R.id.startWork);
        cancelTimerButton = dialog.findViewById(R.id.cancelTimer);
        startWorkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                workTimer ();
                setProgressBarValues(leftTime);
                dialog.cancel();
            }
        });
        cancelTimerButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                resetTimer();
                dialog.cancel();
            }
        });
    }

    private void onFinishedWork() {
        timerState = TimerState.FINISH_WORK;
        updateViews();
        updateSessionCounterWork ();
        finishedWork = true;
        Button continueWorkButton = dialog.findViewById (R.id.continueWorking);
        Button takeABreakButton = dialog.findViewById (R.id.takeABreak);
        Button snoozeButton = dialog.findViewById (R.id.snooze);
        cancelTimerButton = dialog.findViewById(R.id.cancelTimer);
        takeABreakButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                breakTimer();
                setProgressBarValues(breakTime);
                dialog.cancel();
            }
        });

        snoozeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                snoozeTimer();
                setProgressBarValues(snoozeTime);
                dialog.cancel();
            }
        });

        continueWorkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                continueWorkTime();
                setProgressBarValues(continueWorkTime);
                dialog.cancel();
            }
        });

        cancelTimerButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                resetTimer();
                dialog.cancel();
            }
        });
    }

    private void updateSessionCounterWork() {
        if(sessionCounterWork == 0){
            sessionCounterWork = 1;
        }
        int sessionCount = sessionCounterWork++;
        configurationFragmentViewModel.setSessionCounterWork (sessionCount);
    }

    private void updateSessionCounterBreak() {
        if(sessionCounterBreak == 0){
            sessionCounterBreak = 1;
        }
        int sessionCount = sessionCounterBreak++;
        configurationFragmentViewModel.setSessionCounterBreak (sessionCount);
    }


    private void pauseTimer() {
        timerState = TimerState.PAUSE;
        countDownTimer.cancel();
        updateViews();
        ((MainActivity)getActivity()).cancelAlarm ();
    }

    private void resumeTimer()
    {
        timerState = TimerState.WORK;
        updateViews();
        ((MainActivity) getActivity ( )).notify (leftTime,String.valueOf (timerState));
        startTimer();
    }

    private void workTimer()
    {
        timerState = TimerState.WORK;
        updateViews();
        if(startTime == 0) {
            finishedBreak = true;
        }
        if(finishedBreak) {
            startTime = setTime; //setProgressBar
            leftTime = setTime; //setRealTime
            finishedBreak = false;
        }
        else {
            leftTime = startTime;
        }
        ((MainActivity) getActivity ( )).notify (leftTime,String.valueOf (timerState));
        startTimer();
    }

    private void breakTimer()
    {
        timerState = TimerState.BREAK;
        updateViews();
        if(breakTime == 0) {
            finishedWork = true;
        }
        if(finishedWork) {
            breakTime = setBreakTime; //setProgressBar
            leftTime = setBreakTime; //setRealTime
            finishedWork = false;
            finishedBreak = true;
        }
        else {
            leftTime = breakTime;
        }
        ((MainActivity) getActivity ( )).notify (leftTime,String.valueOf (timerState));
        startTimer( );
    }

    private void snoozeTimer()
    {
        timerState = TimerState.SNOOZE;
        updateViews();
        if(finishedWork) {
            snoozeTime = setSnoozeTime; //setProgressBar
            leftTime = setSnoozeTime; //setRealTime
            finishedWork = false;
        } else {
            leftTime = snoozeTime;
        }
        ((MainActivity) getActivity ( )).notify (leftTime,String.valueOf (timerState));
        startTimer();
    }
    private void continueWorkTime()
    {
        timerState = TimerState.CONTINUE;
        updateViews();
        if(finishedWork) {
            continueWorkTime = setTime; //setProgressBar
            leftTime = setTime; //setRealTime
            finishedWork = false;
        } else {
            leftTime = continueWorkTime;
        }
        ((MainActivity) getActivity ( )).notify (leftTime,String.valueOf (timerState));
        startTimer();
    }

    private void resetTimer() {
        timerState = TimerState.RESET;
        snoozeTime = setSnoozeTime;
        breakTime = setBreakTime;
        finishedBreak = false;
        finishedWork = false;
        ((MainActivity)getActivity()).cancelAlarm ();
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
                startButton.setEnabled(false);
                timePicker.setEnabled(false);
                startButton.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.INVISIBLE);
                snoozeIcon.setVisibility(View.INVISIBLE);
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case PAUSE:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                startButton.setEnabled(false);
                timePicker.setEnabled(false);
                startButton.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                break;
            case SNOOZE:
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                startButton.setEnabled(false);
                timePicker.setEnabled(false);
                reset.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                snoozeIcon.setVisibility(View.VISIBLE);
                pauseResume.setEnabled(false);
                breakIcon.setVisibility(View.INVISIBLE);
                break;
            case BREAK:
                progressBar.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                startButton.setEnabled(false);
                timePicker.setEnabled(false);
                reset.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.VISIBLE);
                snoozeIcon.setVisibility(View.INVISIBLE);
                pauseResume.setEnabled(false);
                break;
            case CONTINUE:
                progressBar.setVisibility(View.VISIBLE);
                pauseResume.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                reset.setEnabled(true);
                pauseResume.setEnabled(true);
                startButton.setEnabled(false);
                timePicker.setEnabled(false);
                startButton.setVisibility(View.INVISIBLE);
                messageText.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.INVISIBLE);
                snoozeIcon.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.INVISIBLE);
                pauseResume.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case FINISH_WORK:
                dialog=new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
                dialog.setContentView(R.layout.alert_dialog_break);
                dialog.setCancelable(false);
                dialog.show();
                messageText.setVisibility(View.INVISIBLE);
                time.setText(R.string.timeLeftFormatted);
                break;
            case START_WORK:
                dialog=new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
                dialog.setContentView(R.layout.alert_dialog_work);
                dialog.setCancelable(false);
                dialog.show();
                messageText.setVisibility(View.INVISIBLE);
                time.setText(R.string.timeLeftFormatted);
                break;
            case RESET:
                timePicker.setEnabled(true);
                startButton.setEnabled (false);
                progressBar.setVisibility(View.INVISIBLE);
                pauseResume.setVisibility(View.INVISIBLE);
                pauseResume.setVisibility(View.INVISIBLE);
                transparentBackground.setVisibility(View.VISIBLE );
                reset.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
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
                        Animation slideUpText = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up_text);
                        messageText.startAnimation (slideUpText);
                        slideUpText.setAnimationListener (new Animation.AnimationListener ( ) {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                messageText.setVisibility (View.VISIBLE);
                                startButton.setEnabled (true);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                reset.setEnabled(false);
                pauseResume.setEnabled(false);
                pauseResume.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                snoozeIcon.setVisibility(View.INVISIBLE);
                breakIcon.setVisibility(View.INVISIBLE);
                String text = String.format(Locale.getDefault(), "%02d:%02d:00",
                       timePicker.getHour (),timePicker.getMinute () );
                time.setText(text);
                break;
        }

    }


}