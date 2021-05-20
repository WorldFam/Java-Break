package com.example.javabreak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsFragmentViewModel extends ViewModel {
    MutableLiveData<Boolean> vibration;
    MutableLiveData<Boolean> led;
    MutableLiveData<Integer> sound;

    public SettingsFragmentViewModel(){
        vibration = new MutableLiveData<Boolean>();
        led = new MutableLiveData<Boolean>();
        sound = new MutableLiveData<Integer>();
    }

    public LiveData<Boolean> getLedValue() {
        return led;
    }

    public void setLedValue(Boolean ledValueData) {
        led.postValue (ledValueData);
    }

    public LiveData<Boolean> getVibrationValue() {
        return vibration;
    }

    public void setVibrationValue(Boolean vibrationValueData) { vibration.postValue (vibrationValueData);
    }
    public LiveData<Integer> getSoundValue() { return sound; }

    public void setSoundValue(Integer soundValueData) { sound.postValue (soundValueData);
    }

}
