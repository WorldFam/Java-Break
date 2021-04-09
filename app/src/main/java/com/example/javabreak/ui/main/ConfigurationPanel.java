package com.example.javabreak.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.R;
import com.example.javabreak.viewmodel.ConfigurationPanelViewModel;

import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

public class ConfigurationPanel extends AppCompatActivity {

    ImageButton configurationExitButton;

    int breakTime,
            snoozeTime;
    ConfigurationPanelViewModel configurationPanelViewModel;
    SeekBar breakSlider, snoozeSlider;
    TextView breakTimeText, snoozeTimeText;
    private static final int INTERVAL = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_panel);
        configurationPanelViewModel = new ViewModelProvider(this ).get(ConfigurationPanelViewModel.class);

        configurationExitButton = findViewById(R.id.configurationExitButton);
        breakSlider = findViewById(R.id.breakSlider);
        snoozeSlider = findViewById(R.id.snoozeSlider);
        breakTimeText = findViewById(R.id.breakTimeText);
        snoozeTimeText = findViewById(R.id.snoozeTimeText);

        snoozeSlider.setMin(1);
        snoozeSlider.setMax(15);
        snoozeSlider.setProgress(5);


        breakSlider.setMin(1);
        breakSlider.setMax(12);
        breakSlider.setProgress(6);

        breakTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",breakSlider.getProgress()*5));
        snoozeTimeText.setText(String.format(Locale.getDefault(),"%2d minutes",snoozeSlider.getProgress()));
        configurationExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"fadein-to-fadeout");
    }


}
