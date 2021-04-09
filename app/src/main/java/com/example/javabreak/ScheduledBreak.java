package com.example.javabreak;

public class ScheduledBreak {
    private String name;
    private String workDurationText;
    private String breakDurationText;
    private long workDuration;
    private long breakDuration;
    private int breakFrequency;
    public ScheduledBreak(String name, long workDuration, long breakDuration, int breakFrequency) {
        this.name = name;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.breakFrequency = breakFrequency;
    }

    public ScheduledBreak(String name) {
        this.name = name;
    }

    public ScheduledBreak(String name, String workDurationText, String breakDurationText) {
        this.name = name;
        this.workDurationText = workDurationText;
        this.breakDurationText = breakDurationText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(long workDuration) {
        this.workDuration = workDuration;
    }

    public long getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(long breakDuration) {
        this.breakDuration = breakDuration;
    }

    public int getBreakFrequency() {
        return breakFrequency;
    }

    public void setBreakFrequency(int breakFrequency) {
        this.breakFrequency = breakFrequency;
    }

    public String getWorkDurationText() {
        return workDurationText;
    }

    public void setWorkDurationText(String workDurationText) {
        this.workDurationText = workDurationText;
    }

    public String getBreakDurationText() {
        return breakDurationText;
    }

    public void setBreakDurationText(String breakDurationText) {
        this.breakDurationText = breakDurationText;
    }
}
