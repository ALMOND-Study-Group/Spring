package com.Schedule.Schedule.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleRequest(
        String title,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        String location,
        String memo
) {
}
