package com.example.javabreak.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DayOfTheWeekConverter {
    @TypeConverter
    public String toJson(List<DayOfTheWeek> dayOfTheWeeks) {
        if (dayOfTheWeeks == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DayOfTheWeek>>() {}.getType();
        return gson.toJson(dayOfTheWeeks, type);
    }

    @TypeConverter
    public List<DayOfTheWeek> fromJson(String dayOfTheWeeks) {
        if (dayOfTheWeeks == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<DayOfTheWeek>>() {}.getType();
        return gson.fromJson(dayOfTheWeeks, type);
    }
}
