package com.example.javabreak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.javabreak.models.DayOfTheWeek;

import java.util.List;

public class NewReminderFragmentViewModel extends ViewModel {
    MutableLiveData<String> name;
    MutableLiveData<List<DayOfTheWeek>> weekDay;
    MutableLiveData<String> workFrom;
    MutableLiveData<String> workTo;
    MutableLiveData<String> breakFrequency;
    MutableLiveData<String> breakDuration;

    public NewReminderFragmentViewModel(){
        name = new MutableLiveData<>();
        weekDay = new MutableLiveData<>();
        workFrom = new MutableLiveData<>();
        workTo = new MutableLiveData<>();
        breakFrequency = new MutableLiveData<>();
        breakDuration = new MutableLiveData<>();
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public LiveData<List<DayOfTheWeek>> getWeekDay() {
        return weekDay ;
    }

    public void setWeekDay(List<DayOfTheWeek> weekDay) {
        this.weekDay.setValue(weekDay);
    }

    public LiveData<String> getWorkFrom() {
        return workFrom;
    }

    public void setWorkFom(String workFrom) {
        this.workFrom.setValue(workFrom);
    }


    public LiveData<String> getWorkTo() {
        return workTo;
    }

    public void setWorkTo(String workTo) {
        this.workTo.setValue(workTo);
    }


    public LiveData<String> getBreakFrequency() {
        return breakFrequency;
    }

    public void setBreakFrequency(String breakFrequency) {
        this.breakFrequency.setValue(breakFrequency);
    }

    public LiveData<String> getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(String breakDuration) {
        this.breakDuration.setValue(breakDuration);
    }
}
