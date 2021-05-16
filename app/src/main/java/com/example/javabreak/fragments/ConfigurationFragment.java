package com.example.javabreak.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;
import com.example.javabreak.viewmodels.ConfigurationFragmentViewModel;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ConfigurationFragment extends Fragment {

    ImageButton configurationExitButton;

    int breakTime,
            snoozeTime;
    ConfigurationFragmentViewModel configurationFragmentViewModel;
    SeekBar breakSlider, snoozeSlider;
    TextView breakTimeText, snoozeTimeText, sessionCounterWork,sessionCounterBreak;
    SwitchCompat autoStartWork, autoStartBreak;
    private static final int INTERVAL = 5;
    boolean autoStartBreakValue,autoStartWorkValue;
    String sessionCountWork, sessionCountBreak;

    //Does not allow Fragment to be swappable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).lockViewPager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_fragment, container, false);
        configurationFragmentViewModel = new ViewModelProvider(requireActivity() ).get(ConfigurationFragmentViewModel.class);
        loadData();
        configurationExitButton = view.findViewById(R.id.configurationExitButton);
        breakSlider = view.findViewById(R.id.breakSlider);
        snoozeSlider = view.findViewById(R.id.snoozeSlider);
        breakTimeText = view.findViewById(R.id.breakTimeText);
        snoozeTimeText = view.findViewById(R.id.snoozeTimeText);
        autoStartWork = view.findViewById(R.id.autoStart);
        autoStartBreak = view.findViewById(R.id.autoStartBreak);
        sessionCounterWork = view.findViewById(R.id.sessionCounter);
        sessionCounterBreak = view.findViewById(R.id.sessionCounterBreak);

        configurationExitButton.setBackgroundColor(Color.TRANSPARENT);

        snoozeSlider.setMin(1);
        snoozeSlider.setMax(5);
        snoozeSlider.setProgress(snoozeTime);

        breakSlider.setMin(1);
        breakSlider.setMax(12);
        breakSlider.setProgress(breakTime);

        autoStartWork.setChecked(autoStartWorkValue);
        autoStartBreak.setChecked(autoStartBreakValue);

        sessionCounterWork.setText(sessionCountWork);
        sessionCounterBreak.setText(sessionCountBreak);

        configurationFragmentViewModel.getSessionCounterWork ().observe (getActivity ( ), new Observer<Integer> ( ) {
            @Override
            public void onChanged(Integer integer) {
                sessionCounterWork.setText (String.valueOf (integer));
            }
        });

        configurationFragmentViewModel.getSessionCounterBreak ().observe (getActivity ( ), new Observer<Integer> ( ) {
            @Override
            public void onChanged(Integer integer) {
                sessionCounterBreak.setText (String.valueOf (integer));
            }
        });

        breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakSlider.getProgress()*INTERVAL));
        if(snoozeTime == 1)
        {
            snoozeTimeText.setText(String.format(Locale.getDefault(),"%2d minute",snoozeSlider.getProgress( )));
        }
        else {
            snoozeTimeText.setText(String.format(Locale.getDefault( ), "%2d minutes", snoozeSlider.getProgress( )));
        }
        configurationExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity( )).openViewPager( );
            }
        });

        breakSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                breakTime =  breakSlider.getProgress();
                configurationFragmentViewModel.setBreakTime(breakTime*INTERVAL);
                breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakTime*INTERVAL));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        snoozeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                snoozeTime = snoozeSlider.getProgress();
                configurationFragmentViewModel.setSnoozeTime(snoozeTime);
                if(snoozeTime == 1) {
                    snoozeTimeText.setText(String.format(Locale.getDefault(),"%2d minute",snoozeTime));
                }
                else
                    snoozeTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",snoozeTime));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

       autoStartWork.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               configurationFragmentViewModel.setAutoStartWorkValue (isChecked);
           }
       });

        autoStartBreak.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configurationFragmentViewModel.setAutoStartBreakValue (isChecked);
            }
        });

        return view;
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("configurations", MODE_PRIVATE);

        autoStartBreakValue = sharedPreferences.getBoolean("autoStartBreakValue", false);
        autoStartWorkValue = sharedPreferences.getBoolean("autoStartWorkValue", false);

        breakTime = sharedPreferences.getInt("breakTime", 3);
        snoozeTime = sharedPreferences.getInt("snoozeTime", 0);

        sessionCountWork = sharedPreferences.getString ("sessionCounter", "0");
        sessionCountBreak = sharedPreferences.getString ("sessionCounterBreak", "0");
    }


    @Override
    public void onStop() {
        super.onStop();
        saveData( );
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("configurations", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

         autoStartBreakValue = autoStartBreak.isChecked();
         autoStartWorkValue = autoStartWork.isChecked();

        editor.putInt("breakTime", breakTime);
        editor.putInt("snoozeTime",snoozeTime);

        editor.putBoolean("autoStartBreakValue", autoStartBreakValue);
        editor.putBoolean("autoStartWorkValue",autoStartWorkValue);

        String sessionCount = sessionCounterWork.getText ().toString ();
        editor.putString ("sessionCounter",sessionCount);

        String sessionCountBreak = sessionCounterBreak.getText ().toString ();
        editor.putString ("sessionCounterBreak",sessionCountBreak);

        editor.apply();
    }

}
