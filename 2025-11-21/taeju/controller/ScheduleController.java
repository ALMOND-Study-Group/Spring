package com.Schedule.Schedule.controller;


import com.Schedule.Schedule.dto.ScheduleRequest;
import com.Schedule.Schedule.model.ScheduleEntry;
import com.Schedule.Schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*") // Postman 테스트용
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 전체 조회
    @GetMapping
    public List<ScheduleEntry> getAll() {
        return scheduleService.getAll();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ScheduleEntry getById(@PathVariable Long id) {
        return scheduleService.getById(id);
    }

    // 생성
    @PostMapping
    public ScheduleEntry create(@RequestBody ScheduleRequest request) {
        return scheduleService.create(request);
    }

    // 수정
    @PutMapping("/{id}")
    public ScheduleEntry update(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        return scheduleService.update(id, request);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "삭제되었습니다."));
    }

    // 주간 시간표 (30분 단위)
    @GetMapping("/weekly-table")
    public List<List<Object>> getWeeklyTable() {
        return scheduleService.getWeeklyTable();
    }

    // 통계
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return scheduleService.getStats();
    }

    // 제목으로 색상 확인
    @GetMapping("/colors/{title}")
    public Map<String, String> getColor(@PathVariable String title) {
        return Map.of("color", scheduleService.colorService.getColor(title));
    }

    // 요일 목록
    @GetMapping("/days-of-week")
    public List<String> getDaysOfWeek() {
        return Arrays.stream(DayOfWeek.values())
                .map(Enum::name)
                .toList();
    }
}