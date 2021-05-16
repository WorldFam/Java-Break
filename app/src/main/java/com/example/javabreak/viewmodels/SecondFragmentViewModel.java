package com.example.javabreak.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.javabreak.models.ScheduledReminder;
import com.example.javabreak.repository.ReminderRepository;

import java.util.List;

public class SecondFragmentViewModel extends AndroidViewModel {
    private final ReminderRepository repository;
    private final LiveData<List<ScheduledReminder>> allNotes;
    public SecondFragmentViewModel(Application app) {
        super(app);
        repository = ReminderRepository.getInstance(app);
        allNotes = repository.getAllReminders();
    }

    public LiveData<List<ScheduledReminder>> getAllReminders() {
        return allNotes;
    }

    public void getReminder(int scheduledBreak) {
        repository.getReminder (scheduledBreak);
    }

    public void insert(ScheduledReminder scheduledReminder) {
        repository.insert(scheduledReminder);
    }

    public void delete(ScheduledReminder scheduledReminder) {
        repository.delete(scheduledReminder);
    }

}
