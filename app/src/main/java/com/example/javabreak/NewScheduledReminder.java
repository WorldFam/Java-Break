package com.example.javabreak;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Locale;

public class NewScheduledReminder extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    //WEEK TAB AND REST OF DECLARATIONS
    Button workDurationButton,weekDaysButton,breakDurationButton;
    CheckBox Monday,Tuesday, Wednesday,Thursday,Friday,Saturday,Sunday;
    LinearLayout weekDayTab;
    RelativeLayout workDurationTab,breakDurationTab;
    Handler handler = new Handler();
    TabState tabState;
    //ANIMATION
    Animation slideDown;
    ScaleAnimation slideUp;
    //WORK TAB
    Button workFrom, workTo;
    TextView workFromText, workToText;
    TimePickerDialog timePickerDialog;
    //BREAK TAB
    SeekBar breakSlider;
    TextView breakTimeText;
    private static final int INTERVAL = 5;
    //BREAK FREQUENCY TAB
    View view1 = this.getCurrentFocus();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_scheduled_reminder);
        OnCreateParameters();

        weekDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabState = TabState.REPEAT;
                tabManager();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(weekDaysButton.getApplicationWindowToken(), 0);
            }
        });
        workDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabState = TabState.WORK_DURATION;
                tabManager();
            }
        });


        breakDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabState = TabState.BREAK_DURATION;
                tabManager();
            }
        });

        workFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetWorkDuration("Work time from");
            }
        });

        workTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetWorkDuration("Work time to");
            }
        });

        breakSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             /*   breakTime =  breakSlider.getProgress();
                configurationPanelViewModel.setBreakTime(breakTime*INTERVAL);*/
                breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",progress*INTERVAL));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void onSetWorkDuration(String tag) {
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(
                NewScheduledReminder.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setThemeDark(true);
        timePickerDialog.setTimeInterval(1, 5, 60);
        timePickerDialog.setAccentColor(Color.parseColor("#808080"));
        timePickerDialog.setOkColor(Color.parseColor("#FF9800"));
        timePickerDialog.setCancelColor(Color.parseColor("#FF9800"));
        timePickerDialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if(view.getTag().equals("Work time from")) {
            String from = pad(hourOfDay) + ':' +
                    pad(minute);
            workFromText.setText(from);
        }
        else {
            String to = pad(hourOfDay) + ':' +
                    pad(minute);
            workToText.setText(to);
        }
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void OnCreateParameters() {
        workDurationButton = findViewById(R.id.workDurationButton);
        weekDaysButton = findViewById(R.id.weekDaysButton);
        breakDurationButton = findViewById(R.id.breakDurationButton);

        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slideUp = new ScaleAnimation(1, 1, 1, 0);

        Monday = findViewById(R.id.Monday);
        Tuesday = findViewById(R.id.Tuesday);
        Wednesday = findViewById(R.id.Wednesday);
        Thursday = findViewById(R.id.Thursday);
        Friday = findViewById(R.id.Friday);
        Saturday = findViewById(R.id.Saturday);
        Sunday = findViewById(R.id.Sunday);

        weekDayTab = findViewById(R.id.weekDayTab);
        workDurationTab = findViewById(R.id.workDurationTab);
        breakDurationTab = findViewById(R.id.breakDurationTab);

        Monday.setVisibility(View.GONE);
        Tuesday.setVisibility(View.GONE);
        Wednesday.setVisibility(View.GONE);
        Thursday.setVisibility(View.GONE);
        Friday.setVisibility(View.GONE);
        Saturday.setVisibility(View.GONE);
        Sunday.setVisibility(View.GONE);

        weekDayTab.setVisibility(View.GONE);
        workDurationTab.setVisibility(View.GONE);
        breakDurationTab.setVisibility(View.GONE);

        workFrom = findViewById(R.id.workFrom);
        workTo = findViewById(R.id.workTo);
        workFromText = findViewById(R.id.workFromText);
        workToText = findViewById(R.id.workToText);

        breakSlider = findViewById(R.id.breakSlider);
        breakTimeText = findViewById(R.id.breakTimeText);
        breakSlider.setMin(1);
        breakSlider.setMax(12);
        breakSlider.setProgress(6);
        breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakSlider.getProgress()*5));
    }

    private void tabManager()
    {
        switch (tabState) {
            case REPEAT:
                shrinkWorkBreakTab(workDurationTab, slideUp, View.GONE, workDurationButton, View.VISIBLE);
                shrinkWorkBreakTab(breakDurationTab, slideUp, View.GONE, breakDurationButton, View.VISIBLE);
                weekDayTab.startAnimation(slideDown);
                weekDayTab.setVisibility(View.VISIBLE);
                weekDaysButton.setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Monday.setVisibility(View.VISIBLE);
                        Tuesday.setVisibility(View.VISIBLE);
                        Wednesday.setVisibility(View.VISIBLE);
                        Thursday.setVisibility(View.VISIBLE);
                        Friday.setVisibility(View.VISIBLE);
                        Saturday.setVisibility(View.VISIBLE);
                        Sunday.setVisibility(View.VISIBLE);
                    }
                }, 100);
                break;
            case WORK_DURATION:
                shrinkWeekDayTab();
                shrinkWorkBreakTab(breakDurationTab, slideUp, View.GONE, breakDurationButton, View.VISIBLE);
                shrinkWorkBreakTab(workDurationTab, slideDown, View.VISIBLE, workDurationButton, View.GONE);
                break;
            case BREAK_DURATION:
                shrinkWeekDayTab();
                shrinkWorkBreakTab(workDurationTab, slideUp, View.GONE, workDurationButton, View.VISIBLE);
                shrinkWorkBreakTab(breakDurationTab, slideDown, View.VISIBLE, breakDurationButton, View.GONE);
                break;
        }
    }


    private void shrinkWeekDayTab() {
        Monday.setVisibility(View.GONE);
        Tuesday.setVisibility(View.GONE);
        Wednesday.setVisibility(View.GONE);
        Thursday.setVisibility(View.GONE);
        Friday.setVisibility(View.GONE);
        Saturday.setVisibility(View.GONE);
        Sunday.setVisibility(View.GONE);

        weekDayTab.startAnimation(slideUp);
        weekDayTab.setVisibility(View.GONE);
        weekDaysButton.setVisibility(View.VISIBLE);
    }

    private void shrinkWorkBreakTab(RelativeLayout workDurationTab, Animation slideUp, int gone, Button workDurationButton, int visible) {
        workDurationTab.startAnimation(slideUp);
        workDurationTab.setVisibility(gone);
        workDurationButton.setVisibility(visible);
    }

}





