package com.example.javabreak.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.javabreak.models.ScheduledBreak;
import com.example.javabreak.repository.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {
    private final ReminderRepository repository;
    private final LiveData<List<ScheduledBreak>> allNotes;
    public ReminderViewModel(Application app) {
        super(app);
        repository = ReminderRepository.getInstance(app);
        allNotes = repository.getAllReminders();
    }

    public LiveData<List<ScheduledBreak>> getAllReminders() {
        return allNotes;
    }

    public void getReminder(int scheduledBreak) {
        repository.getReminder (scheduledBreak);
    }

    public void insert(ScheduledBreak scheduledBreak) {
        repository.insert(scheduledBreak);
    }

    public void delete(ScheduledBreak scheduledBreak) {
        repository.delete(scheduledBreak);
    }

}
