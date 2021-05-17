package com.example.javabreak.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.javabreak.R;

public class ThirdFragment  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        SwitchCompat soundSwitch = view.findViewById (R.id.sound);
        ConstraintLayout constraintLayout;
        constraintLayout = view.findViewById (R.id.third_fragment_component);


        soundSwitch.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    constraintLayout.setVisibility (View.VISIBLE);
                }
                else {constraintLayout.setVisibility (View.GONE);
    }
            }
        });


        return view;
    }

}
