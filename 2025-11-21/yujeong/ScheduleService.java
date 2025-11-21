package com.example.schedule.service;

import com.example.schedule.entity.ScheduleEntry;
import com.example.schedule.dto.ScheduleRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final Map<Long, ScheduleEntry> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    // CRUD
    public List<ScheduleEntry> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(ScheduleEntry::getDayOfWeek).thenComparing(ScheduleEntry::getStartTime))
                .collect(Collectors.toList());
    }

    public Optional<ScheduleEntry> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public ScheduleEntry create(ScheduleRequest req) {
        validateTimes(req.getStartTime(), req.getEndTime());
        // conflict check
        checkConflict(null, req.getDayOfWeek(), req.getStartTime(), req.getEndTime());

        Long id = idGen.getAndIncrement();
        String color = colorForTitle(req.getTitle());
        ScheduleEntry e = new ScheduleEntry(id, req.getTitle(), req.getDayOfWeek(), req.getStartTime(), req.getEndTime(), req.getPlace(), req.getNote(), color);
        storage.put(id, e);
        return e;
    }

    public ScheduleEntry update(Long id, ScheduleRequest req) {
        ScheduleEntry existing = storage.get(id);
        if (existing == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
        validateTimes(req.getStartTime(), req.getEndTime());
        checkConflict(id, req.getDayOfWeek(), req.getStartTime(), req.getEndTime());

        existing.setTitle(req.getTitle());
        existing.setDayOfWeek(req.getDayOfWeek());
        existing.setStartTime(req.getStartTime());
        existing.setEndTime(req.getEndTime());
        existing.setPlace(req.getPlace());
        existing.setNote(req.getNote());
        existing.setColorHex(colorForTitle(req.getTitle()));
        return existing;
    }

    public void delete(Long id) {
        storage.remove(id);
    }

    // 충돌 검사: 같은 요일에서 시간 겹침 허용 X
    private void checkConflict(Long updatingId, DayOfWeek day, LocalTime start, LocalTime end) {
        for (ScheduleEntry other : storage.values()) {
            if (updatingId != null && updatingId.equals(other.getId())) continue;
            if (other.getDayOfWeek() != day) continue;
            // overlap if start < other.end && other.start < end
            if (start.isBefore(other.getEndTime()) && other.getStartTime().isBefore(end)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time conflict with schedule id=" + other.getId());
            }
        }
    }

    private void validateTimes(LocalTime start, LocalTime end) {
        if (start == null || end == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end time required");
        if (!start.isBefore(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time");
    }

    // 주간 시간표(30분 단위) 반환
    public Map<String, List<Map<String, Object>>> weeklyTable() {
        // for each day -> list of 48 slots (00:00 ~ 23:30)
        Map<String, List<Map<String, Object>>> table = new LinkedHashMap<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            List<Map<String, Object>> slots = new ArrayList<>(48);
            LocalTime slotStart = LocalTime.MIDNIGHT;
            for (int i = 0; i < 48; i++) {
                LocalTime slotEnd = slotStart.plusMinutes(30);
                LocalTime finalSlotStart = slotStart;
                LocalTime finalSlotStart1 = slotStart;
                List<Map<String, Object>> occupied = storage.values().stream()
                        .filter(s -> s.getDayOfWeek() == d)
                        .filter(s -> !finalSlotStart.isAfter(s.getEndTime().minusNanos(1)) && finalSlotStart.isBefore(s.getEndTime()))
                        // check if slotStart < schedule.end && schedule.start < slotEnd
                        .filter(s -> finalSlotStart1.isBefore(s.getEndTime()) && s.getStartTime().isBefore(slotEnd))
                        .map(s -> {
                            Map<String, Object> m = new HashMap<>();
                            m.put("id", s.getId());
                            m.put("title", s.getTitle());
                            m.put("color", s.getColorHex());
                            m.put("startTime", s.getStartTime().toString());
                            m.put("endTime", s.getEndTime().toString());
                            return m;
                        })
                        .collect(Collectors.toList());

                Map<String, Object> slotObj = new HashMap<>();
                slotObj.put("start", slotStart.toString());
                slotObj.put("end", slotEnd.toString());
                slotObj.put("occupied", occupied);
                slots.add(slotObj);

                slotStart = slotEnd;
            }
            table.put(d.name(), slots);
        }
        return table;
    }

    // 제목 기반 색상 자동 할당 (결정적)
    public String colorForTitle(String title) {
        if (title == null) title = "default";
        int hash = title.hashCode();
        // make deterministic positive
        int r = (hash >> 16) & 0xFF;
        int g = (hash >> 8) & 0xFF;
        int b = (hash) & 0xFF;
        // avoid very dark colors by shifting
        r = (r + 128) % 256;
        g = (g + 64) % 256;
        b = (b + 32) % 256;
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public Map<String, Object> stats() {
        Map<String, Long> schedulesByDay = Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(DayOfWeek::name, d -> 0L, (a,b)->b, LinkedHashMap::new));

        Map<String, Long> minutesByDay = Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(DayOfWeek::name, d -> 0L, (a,b)->b, LinkedHashMap::new));

        List<ScheduleEntry> all = findAll();
        for (ScheduleEntry s : all) {
            String key = s.getDayOfWeek().name();
            schedulesByDay.put(key, schedulesByDay.getOrDefault(key, 0L) + 1L);
            long minutes = Duration.between(s.getStartTime(), s.getEndTime()).toMinutes();
            minutesByDay.put(key, minutesByDay.getOrDefault(key, 0L) + minutes);
        }

        long total = all.size();
        double avg = total == 0 ? 0 : all.stream()
                .mapToLong(s -> Duration.between(s.getStartTime(), s.getEndTime()).toMinutes())
                .average().orElse(0);

        Map<String, Object> data = new HashMap<>();
        data.put("totalSchedules", total);
        data.put("schedulesByDay", schedulesByDay);
        data.put("minutesByDay", minutesByDay);
        data.put("averageDurationMinutes", (long)Math.round(avg));
        return data;
    }

    // 색상 조회
    public String colorForExistingTitle(String title) {
        return colorForTitle(title);
    }

    // 요일 반환
    public List<String> daysOfWeek() {
        return Arrays.stream(DayOfWeek.values()).map(Enum::name).collect(Collectors.toList());
    }
}
