package com.example.schedule.controller;

import com.example.schedule.entity.ScheduleEntry;
import com.example.schedule.dto.ScheduleRequest;
import com.example.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

        import java.util.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService svc;

    public ScheduleController(ScheduleService svc) {
        this.svc = svc;
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAll() {
        List<ScheduleEntry> all = svc.findAll();
        Map<String,Object> resp = Map.of("success", true, "data", all);
        return ResponseEntity.ok(resp);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getById(@PathVariable Long id) {
        return svc.findById(id)
                .map(e -> ResponseEntity.ok(Map.of("success", true, "data", e)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "error", "Not found")));
    }

    // 생성
    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@RequestBody ScheduleRequest req) {
        ScheduleEntry created = svc.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true, "data", created));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> update(@PathVariable Long id, @RequestBody ScheduleRequest req) {
        ScheduleEntry updated = svc.update(id, req);
        return ResponseEntity.ok(Map.of("success", true, "data", updated));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // 주간 시간표 (30분 단위)
    @GetMapping("/weekly-table")
    public ResponseEntity<Map<String,Object>> weeklyTable() {
        Map<String, Object> table = new HashMap<>();
        table.put("table", svc.weeklyTable());
        return ResponseEntity.ok(Map.of("success", true, "data", table));
    }

    // 통계
    @GetMapping("/stats")
    public ResponseEntity<Map<String,Object>> stats() {
        Map<String, Object> data = svc.stats();
        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    // 제목 기반 색상
    @GetMapping("/colors/{title}")
    public ResponseEntity<Map<String,Object>> colorForTitle(@PathVariable String title) {
        String color = svc.colorForExistingTitle(title);
        return ResponseEntity.ok(Map.of("success", true, "data", Map.of("title", title, "color", color)));
    }

    // 요일 목록
    @GetMapping("/days-of-week")
    public ResponseEntity<Map<String,Object>> daysOfWeek() {
        return ResponseEntity.ok(Map.of("success", true, "data", svc.daysOfWeek()));
    }
}
