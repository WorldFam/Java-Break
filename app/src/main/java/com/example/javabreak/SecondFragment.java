package com.example.javabreak;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.javabreak.viewmodel.SecondFragmentViewModel;
import com.google.android.material.slider.Slider;

public class SecondFragment extends Fragment {

    int breakTime,
            snoozeTime;
     SecondFragmentViewModel secondFragmentViewModel;
     Slider breakSlider, snoozeSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.fragment_second, container, false);
        breakSlider = view.findViewById(R.id.breakSlider);
        snoozeSlider = view.findViewById(R.id.snoozeSlider);
        breakSlider.setValue(15);
        snoozeSlider.setValue(5);

        breakSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                breakTime = (int) slider.getValue();
                secondFragmentViewModel.setBreakTime(breakTime);
            }
        });

        snoozeSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                snoozeTime = (int) slider.getValue();
                secondFragmentViewModel.setSnoozeTime(snoozeTime);
            }

        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         secondFragmentViewModel = new ViewModelProvider(requireActivity()).get(SecondFragmentViewModel.class);
    }



}