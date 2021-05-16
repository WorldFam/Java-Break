package com.example.javabreak.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.javabreak.dao.ReminderDao;
import com.example.javabreak.models.ScheduledReminder;

@Database(entities = {ScheduledReminder.class}, version = 5)
public abstract class ReminderDatabase extends RoomDatabase {
    private static ReminderDatabase instance;

    public abstract ReminderDao reminderDao();

    public static synchronized ReminderDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context,
                    ReminderDatabase.class, "reminder_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
