package com.example.javabreak;

import android.content.SharedPreferences;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.activities.MainActivity;
import com.example.javabreak.viewmodel.ConfigurationPanelViewModel;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ConfigurationsFragment extends Fragment {

    ImageButton configurationExitButton;

    int breakTime,
            snoozeTime;
    ConfigurationPanelViewModel configurationPanelViewModel;
    SeekBar breakSlider, snoozeSlider;
    TextView breakTimeText, snoozeTimeText;
    SwitchCompat autoStartWork, autoStartBreak;
    private static final int INTERVAL = 5;
    boolean autoStartBreakValue,autoStartWorkValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).lockViewPager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.configuration_panel, container, false);
        configurationPanelViewModel = new ViewModelProvider(requireActivity() ).get(ConfigurationPanelViewModel.class);
        loadData();

        configurationExitButton = view.findViewById(R.id.configurationExitButton);
        breakSlider = view.findViewById(R.id.breakSlider);
        snoozeSlider = view.findViewById(R.id.snoozeSlider);
        breakTimeText = view.findViewById(R.id.breakTimeText);
        snoozeTimeText = view.findViewById(R.id.snoozeTimeText);
        autoStartWork = view.findViewById(R.id.autoStart);
        autoStartBreak = view.findViewById(R.id.autoStartBreak);

        snoozeSlider.setMin(1);
        snoozeSlider.setMax(5);
        snoozeSlider.setProgress(snoozeTime);

        breakSlider.setMin(1);
        breakSlider.setMax(12);
        breakSlider.setProgress(breakTime);

        autoStartWork.setChecked(autoStartWorkValue);
        autoStartBreak.setChecked(autoStartBreakValue);

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
                configurationPanelViewModel.setBreakTime(breakTime*INTERVAL);
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
                configurationPanelViewModel.setSnoozeTime(snoozeTime);
                if(snoozeTime == 1)
                {
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
               configurationPanelViewModel.setAutoStartWorkValue (isChecked);
           }
       });

        autoStartBreak.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configurationPanelViewModel.setAutoStartBreakValue (isChecked);
            }
        });

        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart( );
//        loadData();
//    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("configurations", MODE_PRIVATE);

        autoStartBreakValue = sharedPreferences.getBoolean("autoStartBreakValue", false);
        autoStartWorkValue = sharedPreferences.getBoolean("autoStartWorkValue", false);

        breakTime = sharedPreferences.getInt("breakTime", 3);
        snoozeTime = sharedPreferences.getInt("snoozeTime", 0);
    }


    @Override
    public  void onStop() {
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

        editor.apply();
    }


    //    @Override
//    public void finish() {
//        super.finish();
//        CustomIntent.customType(this,"fadein-to-fadeout");
//    }


}
