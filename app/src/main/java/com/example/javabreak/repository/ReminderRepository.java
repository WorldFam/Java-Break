package com.example.javabreak.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.javabreak.dao.ReminderDao;
import com.example.javabreak.database.ReminderDatabase;
import com.example.javabreak.models.ScheduledBreak;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderRepository {
    private static ReminderRepository instance;
    private final ReminderDao reminderDao;
    private final LiveData<List<ScheduledBreak>> reminderList;
    private final ExecutorService executorService;

    private ReminderRepository(Application application) {
        ReminderDatabase database = ReminderDatabase.getInstance(application);
        reminderDao = database.reminderDao();
        reminderList = reminderDao.getAllReminders();
        executorService = Executors.newFixedThreadPool(2);
    }

    public static synchronized ReminderRepository getInstance(Application application) {
        if (instance == null)
            instance = new ReminderRepository(application);

        return instance;
    }

    public LiveData<List<ScheduledBreak>> getAllReminders() {
        return reminderList;
    }

    public void insert(ScheduledBreak scheduledBreak) {
        executorService.execute(() -> reminderDao.insert(scheduledBreak));
    }

    public void delete(ScheduledBreak scheduledBreak) {
        executorService.execute(() -> reminderDao.delete(scheduledBreak));
    }
/*
    private static class InsertAsyncTask extends AsyncTask<ScheduledBreak, Void, Void> {
        private final ReminderDao reminderDaoAsync;
        private InsertAsyncTask(ReminderDao noteDao) {
            this.reminderDaoAsync = noteDao;
        }
        @Override
        protected Void doInBackground(ScheduledBreak... scheduledBreaks) {
            reminderDaoAsync.insert(scheduledBreaks[0]);
            return null;
        }
    }*/
}
