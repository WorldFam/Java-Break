package com.example.javabreak.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.javabreak.models.ScheduledReminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insert(ScheduledReminder scheduledReminder);

    @Delete
    void delete(ScheduledReminder scheduledReminder);

    @Query("SELECT * FROM reminder_table ORDER BY id DESC")
    LiveData<List<ScheduledReminder>> getAllReminders();
}
