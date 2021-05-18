package com.example.javabreak.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.javabreak.MainActivity;
import com.example.javabreak.R;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;

public class ThirdFragment  extends Fragment {
    private SoundPool soundPool;
    private int sound1, sound2, sound3, sound4, sound5;
    RadioGroup radioGroup;
    RadioButton radioButton;
    SwitchCompat switchCompatVibration,switchCompatLED;
    TextView sourceCode;
    Vibrator vibrator;
    boolean vibration, led;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        loadData ();
        SwitchCompat soundSwitch = view.findViewById (R.id.sound);
        ConstraintLayout constraintLayout;
        constraintLayout = view.findViewById (R.id.third_fragment_component);

        vibrator = (Vibrator) getActivity ().getSystemService(VIBRATOR_SERVICE);


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
        vibrator  = (Vibrator)getActivity ().getSystemService(VIBRATOR_SERVICE);

        sourceCode = view.findViewById (R.id.sourceCode);
        switchCompatLED = view.findViewById (R.id.switchCompatLED);
        switchCompatVibration = view.findViewById (R.id.switchCompatVibration);
        radioGroup = view.findViewById(R.id.radioGroup);

        switchCompatVibration.setChecked(vibration);
        switchCompatLED.setChecked(led);

        sound1 = soundPool.load(getContext (), R.raw.sound1, 1);
        sound2 = soundPool.load(getContext (), R.raw.sound2, 1);
        sound3 = soundPool.load(getContext (), R.raw.sound3, 1);
        sound4 = soundPool.load(getContext (), R.raw.sound4, 1);
        sound5 = soundPool.load(getContext (), R.raw.sound5, 1);

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

        radioGroup.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);
                Log.d ("index",String.valueOf (index) );
                switch (index){
                    case 0:
                        soundPool.play (sound1,1,1,0,0,1);
                        break;
                    case 2:
                        soundPool.play (sound2,1,1,0,0,1);
                        break;
                    case 4:
                        soundPool.play (sound3,1,1,0,0,1);
                        break;
                    case 6:
                        soundPool.play (sound4,1,1,0,0,1);
                        break;
                    case 8:
                        soundPool.play (sound5,1,1,0,0,1);
                        break;
                }
            }
        });

        switchCompatLED.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                led = isChecked;
                ((MainActivity)getActivity()).led (isChecked);
            }
        });

        switchCompatVibration.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                vibration = isChecked;
                vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.EFFECT_DOUBLE_CLICK));
                ((MainActivity)getActivity()).vibration (isChecked);}
                else vibrator.cancel ();
            }
        });

        sourceCode.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                //Start implicit intent
                String url = "https://github.com/WorldFam/Java-Break";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
        vibration = sharedPreferences.getBoolean ("vibration",false);
        led = sharedPreferences.getBoolean ("led",false);

    }

    @Override
    public  void onStop() {
        super.onStop();
        saveData( );
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        vibration = switchCompatVibration.isChecked ();
        led = switchCompatLED.isChecked ();
        editor.putBoolean ("led",led);
        editor.putBoolean ("vibration",vibration);
        editor.apply ();
    }





}
