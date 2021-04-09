package com.example.javabreak.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigurationPanelViewModel extends ViewModel {
    MutableLiveData<Integer> breakTime;
    MutableLiveData<Integer> snoozeTime;
    MutableLiveData<String> breakTimeText;
    MutableLiveData<String> snoozeTimeText;

    public ConfigurationPanelViewModel(){
        breakTime = new MutableLiveData<Integer>(30);
        snoozeTime = new MutableLiveData<Integer>(5);
        breakTimeText = new MutableLiveData<String>();
        snoozeTimeText = new MutableLiveData<String >();
    }

    public LiveData<Integer> getBreakTime() {
        return breakTime;
    }

    public LiveData<Integer> getSnoozeTime() {
        return snoozeTime;
    }

    public void setSnoozeTime(Integer snoozeTimeData) {
        snoozeTime.postValue(snoozeTimeData);
    }

    public void setBreakTime(Integer breakTimeData) {
        breakTime.postValue(breakTimeData);
    }
}
