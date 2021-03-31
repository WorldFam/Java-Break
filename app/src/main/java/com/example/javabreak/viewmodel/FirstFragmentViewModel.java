package com.example.javabreak.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FirstFragmentViewModel extends ViewModel {
    private MutableLiveData<Integer> breakTime;
    private MutableLiveData<Integer> snoozeTime;

    public FirstFragmentViewModel(){
        breakTime = new MutableLiveData<>();
        snoozeTime = new MutableLiveData<>();
    }


}
