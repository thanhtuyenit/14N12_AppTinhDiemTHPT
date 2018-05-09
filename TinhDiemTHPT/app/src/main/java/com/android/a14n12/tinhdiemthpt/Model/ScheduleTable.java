package com.android.a14n12.tinhdiemthpt.Model;

/**
 * Created by Nhi on 3/19/2018.
 */

public class ScheduleTable {
    private int id;
    private int time;
    private int numberOfPeriod;
    private int day;
    private String nameSubject;

    public ScheduleTable(int id, int time, int numberOfPeriod, String nameSubject, int day) {
        this.id = id;
        this.time = time;
        this.numberOfPeriod = numberOfPeriod;
        this.day = day;
        this.nameSubject = nameSubject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumberOfPeriod() {
        return numberOfPeriod;
    }

    public void setNumberOfPeriod(int numberOfPeriod) {
        this.numberOfPeriod = numberOfPeriod;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
