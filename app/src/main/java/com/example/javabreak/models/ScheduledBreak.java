package com.example.javabreak.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "reminder_table")
public class ScheduledBreak {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String workFromText;
    private String workToText;
    private String breakDurationText;
    private String breakFrequencyText;
    private int workDuration;
    private int breakDuration;
    private int breakFrequency;
    @TypeConverters(DayOfTheWeekConverter.class)
    private List<DayOfTheWeek> dayOfTheWeeks;

    public ScheduledBreak(String name, String workFromText, String workToText, String breakDurationText, String breakFrequencyText, int workDuration, int breakDuration, int breakFrequency, List<DayOfTheWeek> dayOfTheWeeks) {
        this.name = name;
        this.workFromText = workFromText;
        this.workToText = workToText;
        this.breakDurationText = breakDurationText;
        this.breakFrequencyText = breakFrequencyText;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.breakFrequency = breakFrequency;
        this.dayOfTheWeeks = dayOfTheWeeks;
    }

    public ScheduledBreak() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }
    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public int getBreakFrequency() {
        return breakFrequency;
    }

    public void setBreakFrequency(int breakFrequency) {
        this.breakFrequency = breakFrequency;
    }

    public String getBreakDurationText() {
        return breakDurationText;
    }

    public void setBreakDurationText(String breakDurationText) {
        this.breakDurationText = breakDurationText;
    }

    public List<DayOfTheWeek> getDayOfTheWeeks() {
        return dayOfTheWeeks;
    }

    public void setDayOfTheWeeks(List<DayOfTheWeek> dayOfTheWeeks) {
        this.dayOfTheWeeks = dayOfTheWeeks;
    }

    public String getWorkFromText() {
        return workFromText;
    }

    public void setWorkFromText(String workFromText) {
        this.workFromText = workFromText;
    }

    public String getWorkToText() {
        return workToText;
    }

    public void setWorkToText(String workToText) {
        this.workToText = workToText;
    }

    public String getBreakFrequencyText() {
        return breakFrequencyText;
    }

    public void setBreakFrequencyText(String breakFrequencyText) {
        this.breakFrequencyText = breakFrequencyText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
