package com.Schedule.Schedule.model;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Setter
@Getter
public class ScheduleEntry {
    private Long id; // 서버가 관리, 나중에 삭제/수정 할때 사용
    private String title;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String memo;
    private String color; //자동생성
}
