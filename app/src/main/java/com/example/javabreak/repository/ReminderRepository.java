package com.example.javabreak.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.javabreak.dao.ReminderDao;
import com.example.javabreak.database.ReminderDatabase;
import com.example.javabreak.models.ScheduledReminder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderRepository {
    private static ReminderRepository instance;
    private final ReminderDao reminderDao;
    private final LiveData<List<ScheduledReminder>> reminderList;
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

    public LiveData<List<ScheduledReminder>> getAllReminders() {
        return reminderList;
    }

    public void getReminder(int scheduledBreak) {
/*
        new InsertAsyncTask(reminderDao).execute(scheduledBreak);
*/
//        executorService.execute(() -> reminderDao.getReminder(scheduledBreak));
    }


    public void insert(ScheduledReminder scheduledReminder) {
/*
        new InsertAsyncTask(reminderDao).execute(scheduledBreak);
*/
        executorService.execute(() -> reminderDao.insert(scheduledReminder));
    }

    public void delete(ScheduledReminder scheduledReminder) {
/*
        new InsertAsyncTask(reminderDao).execute(scheduledBreak);
*/
        executorService.execute(() -> reminderDao.delete(scheduledReminder));
    }
}
