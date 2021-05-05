package com.example.javabreak.models;

public class DayOfTheWeek {
    String weekDay;
    boolean checked;

    public DayOfTheWeek(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return  weekDay;
    }

    public boolean isChecked() {
        return checked;
    }
}
