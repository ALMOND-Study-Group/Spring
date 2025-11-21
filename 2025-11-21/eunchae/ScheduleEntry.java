package com.example.Schedule2;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ScheduleEntry {
    private Long id;
    private String title;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String memo;

    public ScheduleEntry() {
    }

    public ScheduleEntry(String title, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String location, String memo) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getMemo() {
        return memo;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
