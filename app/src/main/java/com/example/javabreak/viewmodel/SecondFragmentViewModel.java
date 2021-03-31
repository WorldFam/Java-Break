package com.example.javabreak.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecondFragmentViewModel extends ViewModel {
    MutableLiveData<Integer> breakTime;
    MutableLiveData<Integer> snoozeTime;

    public SecondFragmentViewModel(){
        breakTime = new MutableLiveData<Integer>(15);
        snoozeTime = new MutableLiveData<Integer>(5);
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
