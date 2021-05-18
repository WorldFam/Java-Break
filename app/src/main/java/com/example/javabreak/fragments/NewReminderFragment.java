package com.example.javabreak.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;
import com.example.javabreak.models.DayOfTheWeek;
import com.example.javabreak.viewmodels.NewReminderViewModel;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class NewReminderFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    //IMPORTANT DECLARATIONS
    Button workDurationButton, weekDaysButton, breakDurationButton, breakFrequencyButton;
    LinearLayout weekDayTab;
    RelativeLayout workDurationTab, breakDurationTab, breakFrequencyTab;
    TabState tabState;
    boolean weekBool, workBool, breakBool, breakFqBool = false;
    NewReminderViewModel newReminderViewModel;
    ImageView nameView;
    //Name
    EditText nameEditText;
    //WEEK TAB
    ListView listView;
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
    SeekBar breakFrequencySlider;
    TextView breakFrequencyTimeText;
    private static final int INTERVAL_FREQUENCY = 15;
    //CREATE REMINDER
    Button createReminderButton;
    //NOTIFICATION STUFF
    Calendar calendar = Calendar.getInstance();
    int hourOfDayFrom = 9;
    int hourOfDayTo = 15;
    int minuteFrom,minuteTo = 0;
    int breakFrequency = 60;
    int breakDuration = 30;
    //Toast
    Toast toast;

    public enum TabState {
        REPEAT, WORK_DURATION, BREAK_DURATION, BREAK_FREQUENCY;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_reminder_fragment, container, false);
        OnCreateParameters(view);

        newReminderViewModel =  new ViewModelProvider(requireActivity()).get(NewReminderViewModel.class);

        nameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEditText.setCursorVisible(true);
            }
        });


        weekDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabState = TabState.REPEAT;
                tabManager();
                hideKeyboardFrom(view);
            }
        });

        workDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabState = TabState.WORK_DURATION;
                tabManager();
                hideKeyboardFrom(view);
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

        breakDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabState = TabState.BREAK_DURATION;
                tabManager();
                hideKeyboardFrom(view);
                breakSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",progress*INTERVAL));
                        breakDuration = progress * INTERVAL;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });

        breakFrequencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabState = TabState.BREAK_FREQUENCY;
                tabManager();
                hideKeyboardFrom(view);

                breakFrequencySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        breakFrequencyTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",progress*INTERVAL_FREQUENCY));
                        breakFrequency = progress * INTERVAL_FREQUENCY;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });


        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                DayOfTheWeek user = (DayOfTheWeek) listView.getItemAtPosition(position);
                user.setChecked(currentCheck);
            }
        });

        listViewData( );

        createReminderButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().equals("")) {
                    if(toast != null) {
                        toast.cancel();
                    }
                    toast = new Toast(getActivity());
                    toast = Toasty.error(getContext(), "Name tag cannot be empty", Toast.LENGTH_SHORT, true);
                    toast.show();
                    nameView.setImageDrawable(getResources().getDrawable(R.drawable.red_stroke_component));
                }
                else if(nameEditText.getText().toString().length() > 15)
                {
                    if(toast != null) {
                        toast.cancel();
                    }
                    toast = new Toast(getActivity());
                    toast = Toasty.error(getContext(), "Name tag contains more than 15 characters", Toast.LENGTH_SHORT, true);
                    toast.show();
                    nameView.setImageDrawable(getResources().getDrawable(R.drawable.red_stroke_component));
                }
                else if(0 == listView.getCheckedItemCount())
                {
                    if(toast != null) {
                        toast.cancel();
                    }
                    toast = new Toast(getActivity());
                    toast = Toasty.warning(getContext(), "Select week day", Toast.LENGTH_SHORT, true);
                    toast.show();
                    weekDayTab.setBackgroundResource(R.drawable.red_stroke_component);
                    nameView.setImageDrawable(getResources().getDrawable(R.drawable.component_rectangle));
                    if(tabState != TabState.REPEAT) {
                        tabState = TabState.REPEAT;
                        tabManager( );
                    }
                }
                else {
                    if(toast != null) {
                        toast.cancel();
                    }
                    newReminderViewModel.setName(nameEditText.getText( ).toString( ).trim ());
                    newReminderViewModel.setWorkFom(workFromText.getText( ).toString( ));
                    newReminderViewModel.setWorkTo(workToText.getText( ).toString( ));
                    String removeFrequency = breakFrequencyTimeText.getText( ).toString( );
                    String removeBreak = breakTimeText.getText( ).toString( );
                    removeFrequency = removeFrequency.replace("utes", "");
                    removeBreak = removeBreak.replace("utes", "");
                    newReminderViewModel.setBreakFrequency(removeFrequency);
                    newReminderViewModel.setBreakDuration(removeBreak);

                    ((MainActivity) getActivity( )).openViewPager( );

                    SparseBooleanArray sp = listView.getCheckedItemPositions( );
                    List<DayOfTheWeek> list = new ArrayList<>( );
                    for (int i = 0; i < listView.getAdapter( ).getCount( ); i++) {
                        if (sp.get(i)) {
                            DayOfTheWeek dayOfTheWeek = (DayOfTheWeek) listView.getItemAtPosition(i);
                            list.add(dayOfTheWeek);
                        }
                    }
                    newReminderViewModel.setWeekDay(list);

                    for(DayOfTheWeek weekDay : list) {
                        getWeekDay (weekDay);
                    }

                }
            }

        });
        return view;
    }

    private void getWeekDay(DayOfTheWeek dayOfTheWeek) {
        if (dayOfTheWeek.getWeekDay().equals("Monday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.MONDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);
        }

        if (dayOfTheWeek.getWeekDay().equals("Tuesday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.TUESDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
        if (dayOfTheWeek.getWeekDay().equals("Wednesday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.WEDNESDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
        if (dayOfTheWeek.getWeekDay().equals("Thursday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.THURSDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
        if (dayOfTheWeek.getWeekDay().equals("Friday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.FRIDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
        if (dayOfTheWeek.getWeekDay().equals("Saturday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.SATURDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
        if (dayOfTheWeek.getWeekDay().equals("Sunday")) {
            ((MainActivity) getActivity( )).startAlarm (Calendar.SUNDAY,hourOfDayFrom,minuteFrom, breakFrequency,breakDuration);

        }
//        Log.d ("Dayzzz",String.valueOf (calendar.get (Calendar.DAY_OF_WEEK)));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).lockViewPager();
    }


    private void listViewData() {

        DayOfTheWeek Monday = new DayOfTheWeek("Monday");
        DayOfTheWeek Tuesday = new DayOfTheWeek("Tuesday");
        DayOfTheWeek Wednesday = new DayOfTheWeek("Wednesday");
        DayOfTheWeek Thursday = new DayOfTheWeek("Thursday");
        DayOfTheWeek Friday = new DayOfTheWeek("Friday");
        DayOfTheWeek Saturday = new DayOfTheWeek("Saturday");
        DayOfTheWeek Sunday = new DayOfTheWeek("Sunday");

        DayOfTheWeek[] weekDays = new DayOfTheWeek[] {
                Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday
        };

        ArrayAdapter<DayOfTheWeek> arrayAdapter
                = new ArrayAdapter<>(getContext(),R.layout.new_reminder_component, weekDays);

        this.listView.setAdapter(arrayAdapter);
    }

       private void hideKeyboardFrom(View view) {
        nameEditText.setCursorVisible(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void onSetWorkDuration(String tag) {
        Calendar now = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(
                NewReminderFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setThemeDark(true);
        timePickerDialog.setTimeInterval(1, 5, 60);
        timePickerDialog.setAccentColor(Color.parseColor("#808080"));
        timePickerDialog.setOkColor(Color.parseColor("#FF9800"));
        timePickerDialog.setCancelColor(Color.parseColor("#FF9800"));
        timePickerDialog.show(getChildFragmentManager(), tag);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if(view.getTag().equals("Work time from")) {
            String from = pad(hourOfDay) + ':' +
                    pad(minute);
            workFromText.setText(from);
            hourOfDayFrom = hourOfDay;
            minuteFrom = minute;
        }
        else {
            String to = pad(hourOfDay) + ':' +
                    pad(minute);
            workToText.setText(to);
            hourOfDayTo = hourOfDay;
            minuteTo = minute;
        }
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void OnCreateParameters(View view) {
        workDurationButton = view.findViewById(R.id.workDurationButton);
        weekDaysButton = view.findViewById(R.id.weekDaysButton);
        breakDurationButton = view.findViewById(R.id.breakDurationButton);
        breakFrequencyButton = view.findViewById(R.id.breakFrequencyButton);

        nameEditText = view.findViewById(R.id.name_edit_text);

        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slideUp = new ScaleAnimation(1, 1, 1, 0);

        weekDayTab = view.findViewById(R.id.weekDayTab);
        workDurationTab = view.findViewById(R.id.workDurationTab);
        breakDurationTab = view.findViewById(R.id.breakDurationTab);
        breakFrequencyTab = view.findViewById(R.id.breakFrequencyTab);

        weekDayTab.setVisibility(View.GONE);
        workDurationTab.setVisibility(View.GONE);
        breakDurationTab.setVisibility(View.GONE);
        breakFrequencyTab.setVisibility(View.GONE);

        workFrom = view.findViewById(R.id.workFrom);
        workTo = view.findViewById(R.id.workTo);
        workFromText = view.findViewById(R.id.workFromText);
        workToText = view.findViewById(R.id.workToText);

        breakSlider = view.findViewById(R.id.breakSlider);
        breakTimeText = view.findViewById(R.id.breakTimeText);
        breakSlider.setMin(1);
        breakSlider.setMax(12);
        breakSlider.setProgress(6);
        breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakSlider.getProgress()*5));

        breakFrequencySlider = view.findViewById(R.id.breakFrequencySlider);
        breakFrequencyTimeText = view.findViewById(R.id.breakFrequencyTimeText);
        breakFrequencySlider.setMin(1);
        breakFrequencySlider.setMax(12);
        breakFrequencySlider.setProgress(4);
        breakFrequencyTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakSlider.getProgress()*10));

        createReminderButton = view.findViewById(R.id.createReminderButton);

        listView = view.findViewById(R.id.list);

        nameView = view.findViewById(R.id.imageView3);

    }

    private void tabManager()
    {
        switch (tabState) {
            case REPEAT:
                weekBool = true;
                if(workBool) {
                    shrinkWorkBreakFrequencyTab(workDurationTab, slideUp, View.GONE, workDurationButton, View.VISIBLE);
                    workBool = false;
                }
                else if(breakBool){
                    shrinkWorkBreakFrequencyTab(breakDurationTab, slideUp, View.GONE, breakDurationButton, View.VISIBLE);
                    breakBool = false;
                }
                else if(breakFqBool) {
                    shrinkWorkBreakFrequencyTab(breakFrequencyTab, slideUp, View.GONE, breakFrequencyButton, View.VISIBLE);
                    breakFqBool = false;
                }
                weekDayTab.startAnimation(slideDown);
                weekDayTab.setVisibility(View.VISIBLE);
                weekDaysButton.setVisibility(View.GONE);
                break;
            case WORK_DURATION:
                workBool = true;
                if(weekBool) {
                    shrinkWeekDayTab();
                    weekBool = false;
                }
                else if(breakBool) {
                    shrinkWorkBreakFrequencyTab(breakDurationTab, slideUp, View.GONE, breakDurationButton, View.VISIBLE);
                    breakBool = false;
                }
                else if(breakFqBool) {
                    shrinkWorkBreakFrequencyTab(breakFrequencyTab, slideUp, View.GONE, breakFrequencyButton, View.VISIBLE);
                    breakFqBool = false;
                }
                shrinkWorkBreakFrequencyTab(workDurationTab, slideDown, View.VISIBLE, workDurationButton, View.GONE);
                break;
            case BREAK_DURATION:
                breakBool = true;
                if(weekBool) {
                    shrinkWeekDayTab();
                    weekBool = false;
                }
                else if(workBool) {
                    shrinkWorkBreakFrequencyTab(workDurationTab, slideUp, View.GONE, workDurationButton, View.VISIBLE);
                    workBool = false;
                }
                else if(breakFqBool) {
                    shrinkWorkBreakFrequencyTab(breakFrequencyTab, slideUp, View.GONE, breakFrequencyButton, View.VISIBLE);
                    breakFqBool = false;
                }
                shrinkWorkBreakFrequencyTab(breakDurationTab, slideDown, View.VISIBLE, breakDurationButton, View.GONE);
                break;
            case BREAK_FREQUENCY:
                breakFqBool = true;
                if(weekBool) {
                    shrinkWeekDayTab();
                    weekBool = false;
                }
                else if(workBool) {
                    shrinkWorkBreakFrequencyTab(workDurationTab, slideUp, View.GONE, workDurationButton, View.VISIBLE);
                    workBool = false;
                }
                else if(breakBool) {
                    shrinkWorkBreakFrequencyTab(breakDurationTab, slideUp, View.GONE, breakDurationButton, View.VISIBLE);
                    breakBool = false;
                }
                shrinkWorkBreakFrequencyTab(breakFrequencyTab, slideDown, View.VISIBLE, breakFrequencyButton, View.GONE);
                break;

        }
    }


    private void shrinkWeekDayTab() {
        weekDayTab.startAnimation(slideUp);
        weekDayTab.setVisibility(View.GONE);
        weekDaysButton.setVisibility(View.VISIBLE);
    }

    private void shrinkWorkBreakFrequencyTab(RelativeLayout workDurationTab, Animation slideUp, int gone, Button workDurationButton, int visible) {
        workDurationTab.startAnimation(slideUp);
        workDurationTab.setVisibility(gone);
        workDurationButton.setVisibility(visible);
    }

}





