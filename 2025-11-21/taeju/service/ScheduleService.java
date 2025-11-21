package com.Schedule.Schedule.service;

import com.Schedule.Schedule.dto.ScheduleRequest;
import com.Schedule.Schedule.model.ScheduleEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    // 메모리 대신 저장소 (DB 없으니까 List로!)
    private final List<ScheduleEntry> schedules = new ArrayList<>();
    private final Map<Long, ScheduleEntry> scheduleMap = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    private final ColorService colorService;

    public ScheduleService(ColorService colorService) {
        this.colorService = colorService;
    }

    // 테스트용 데이터 (서버 시작하자마자 몇 개 넣어놓기)
    @PostConstruct
    public void init() {
        create(new ScheduleRequest("알고리즘 공부", DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), "집", "백준 풀기"));
        create(new ScheduleRequest("영어 회화", DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "줌", "원어민 수업"));
        create(new ScheduleRequest("운동", DayOfWeek.TUESDAY, LocalTime.of(18, 0), LocalTime.of(19, 30), "헬스장", "하체 데이"));
        create(new ScheduleRequest("알고리즘 공부", DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), "집", "프로그래머스"));
    }

    // CREATE
    public ScheduleEntry create(ScheduleRequest request) {
        // 1. 시간 충돌 검사
        if (hasTimeConflict(request)) {
            throw new IllegalArgumentException("해당 시간대에 이미 일정이 있습니다: " + request.dayOfWeek() + " " + request.startTime() + "~" + request.endTime());
        }

        // 2. 새 일정 만들기
        ScheduleEntry entry = new ScheduleEntry();
        entry.setId(nextId++);
        entry.setTitle(request.title());
        entry.setDayOfWeek(request.dayOfWeek());
        entry.setStartTime(request.startTime());
        entry.setEndTime(request.endTime());
        entry.setLocation(request.location());
        entry.setMemo(request.memo());
        entry.setColor(colorService.getColor(request.title())); // 자동 색상

        schedules.add(entry);
        scheduleMap.put(entry.getId(), entry);

        return entry;
    }

    // 시간 충돌 검사 (핵심 로직!)
    private boolean hasTimeConflict(ScheduleRequest request) {
        return schedules.stream()
                .filter(s -> s.getDayOfWeek() == request.dayOfWeek())
                .anyMatch(existing -> {
                    // 새 일정: 10:00 ~ 11:00
                    // 기존: 10:30 ~ 11:30 → 겹침!
                    return !request.startTime().isBefore(existing.getStartTime()) && // 새 시작이 기존 시작보다 늦거나 같고
                            !request.endTime().isAfter(existing.getEndTime()) ||    // 새 끝이 기존 끝보다 빠르거나 같으면 → 완전히 포함
                            request.startTime().isBefore(existing.getEndTime()) &&  // 새 시작이 기존 끝보다 전
                                    request.endTime().isAfter(existing.getStartTime());     // 새 끝이 기존 시작보다 후 → 겹침!
                });
    }

    // READ ALL
    public List<ScheduleEntry> getAll() {
        return new ArrayList<>(schedules);
    }

    // READ ONE
    public ScheduleEntry getById(Long id) {
        ScheduleEntry entry = scheduleMap.get(id);
        if (entry == null) {
            throw new NoSuchElementException("ID가 " + id + "인 일정이 없습니다.");
        }
        return entry;
    }

    // UPDATE
    public ScheduleEntry update(Long id, ScheduleRequest request) {
        ScheduleEntry existing = getById(id);

        // 기존 일정 빼고 충돌 검사 (자기 자신은 제외)
        schedules.remove(existing);

        ScheduleRequest tempRequest = new ScheduleRequest(
                request.title(), request.dayOfWeek(), request.startTime(),
                request.endTime(), request.location(), request.memo()
        );

        if (hasTimeConflict(tempRequest)) {
            schedules.add(existing); // 원복
            throw new IllegalArgumentException("수정 시 시간 충돌 발생!");
        }

        // 수정 적용
        existing.setTitle(request.title());
        existing.setDayOfWeek(request.dayOfWeek());
        existing.setStartTime(request.startTime());
        existing.setEndTime(request.endTime());
        existing.setLocation(request.location());
        existing.setMemo(request.memo());
        existing.setColor(colorService.getColor(request.title()));

        // 다시 추가
        schedules.add(existing);
        return existing;
    }

    // DELETE
    public void delete(Long id) {
        ScheduleEntry entry = getById(id);
        schedules.remove(entry);
        scheduleMap.remove(id);
    }

    // 주간 시간표 (30분 단위) - 프론트엔드에서 그대로 그리면 됨!
    public List<List<Object>> getWeeklyTable() {
        List<List<Object>> table = new ArrayList<>();

        for (int i = 0; i < 48; i++) { // 00:00 ~ 23:30
            LocalTime time = LocalTime.of(0, 0).plusMinutes(i * 30);
            List<Object> row = new ArrayList<>();
            row.add(time.toString()); // "09:30"

            for (DayOfWeek day : DayOfWeek.values()) {
                LocalTime finalTime = time;

                Optional<Map<String, String>> schedule = schedules.stream()
                        .filter(s -> s.getDayOfWeek() == day)
                        .filter(s -> !finalTime.isBefore(s.getStartTime()) && finalTime.isBefore(s.getEndTime()))
                        .map(s -> Map.of(
                                "id", String.valueOf(s.getId()),
                                "title", s.getTitle(),
                                "color", s.getColor(),
                                "location", s.getLocation() != null ? s.getLocation() : ""
                        ))
                        .findFirst();

                row.add(schedule.orElse(null));
            }
            table.add(row);
        }
        return table;
    }

    // 통계 API (문제에서 나온 그 JSON!)
    public Map<String, Object> getStats() {
        long total = schedules.size();

        Map<DayOfWeek, Long> countByDay = schedules.stream()
                .collect(Collectors.groupingBy(ScheduleEntry::getDayOfWeek, Collectors.counting()));

        Map<DayOfWeek, Long> minutesByDay = schedules.stream()
                .collect(Collectors.groupingBy(
                        ScheduleEntry::getDayOfWeek,
                        Collectors.summingLong(s -> ChronoUnit.MINUTES.between(s.getStartTime(), s.getEndTime()))
                ));

        double avgMinutes = schedules.stream()
                .mapToLong(s -> ChronoUnit.MINUTES.between(s.getStartTime(), s.getEndTime()))
                .average()
                .orElse(0.0);

        Map<String, Long> schedulesByDay = new LinkedHashMap<>();
        Map<String, Long> minutesByDayMap = new LinkedHashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            schedulesByDay.put(day.name(), countByDay.getOrDefault(day, 0L));
            minutesByDayMap.put(day.name(), minutesByDay.getOrDefault(day, 0L));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("totalSchedules", total);
        data.put("schedulesByDay", schedulesByDay);
        data.put("minutesByDay", minutesByDayMap);
        data.put("averageDurationMinutes", Math.round(avgMinutes));

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);

        return result;
    }
}
