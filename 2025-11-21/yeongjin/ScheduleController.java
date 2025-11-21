package com.example.bootdailymission.daily1010;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bootdailymission.daily1010.ScheduleService.*;

import java.time.DayOfWeek;
import java.util.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @GetMapping
    public ResponseEntity<List<ScheduleEntry>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleEntry> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScheduleEntry> create(@RequestBody ScheduleRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleEntry> update(@PathVariable Long id,
                                                @RequestBody ScheduleRequest req) {
        try {
            return ResponseEntity.ok(service.update(id, req));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/weekly-table")
    public ResponseEntity<Map<DayOfWeek, List<Slot>>> weeklyTable() {
        return ResponseEntity.ok(service.getWeeklyTable());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        Stats s = service.getStats();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", s);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/colors/{title}")
    public ResponseEntity<Map<String, String>> getColor(@PathVariable String title) {
        String color = service.generateColor(title);
        return ResponseEntity.ok(Collections.singletonMap("color", color));
    }

    @GetMapping("/days-of-week")
    public ResponseEntity<List<String>> daysOfWeek() {
        List<String> days = new ArrayList<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            days.add(d.name());
        }
        return ResponseEntity.ok(days);
    }
}