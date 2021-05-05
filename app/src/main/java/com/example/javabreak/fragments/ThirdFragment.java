package com.example.javabreak.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javabreak.R;
import com.example.javabreak.viewmodel.ScheduledSecondSharedViewModel;

import java.text.DecimalFormat;

public class ThirdFragment  extends Fragment {
    private static final int INTERVAL = 5;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    private TimePicker picker; // set in onCreate
    private NumberPicker minutePicker;
    EditText editText;
    Button button;
    ScheduledSecondSharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        picker = view.findViewById(R.id.timePicker);
       /* picker.setIs24HourView(true);
        setMinutePicker();
        viewModel =  new ViewModelProvider(requireActivity()).get(ScheduledSecondSharedViewModel.class);
        editText = view.findViewById(R.id.editTextTextPersonName123);
        button = view.findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setName(editText.getText().toString());
            }
        });*/

        return view;
    }
    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = picker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }


    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return picker.getMinute();
        }
    }
}