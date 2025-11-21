package com.example.bootdailymission.daily1010;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final Map<Long, ScheduleEntry> store = new ConcurrentHashMap<>();
    // AtomicLong을 이용하여 원자성, 동시성 보장
    private final AtomicLong seq = new AtomicLong(1);

    @PostConstruct
    public void initSampleData() {
        // MONDAY
        create(new ScheduleRequest("수학", DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 30),
                "101호", "교재 3장 복습"));
        create(new ScheduleRequest("영어 회의", DayOfWeek.MONDAY,
                LocalTime.of(11, 0), LocalTime.of(12, 0),
                "회의실 A", "팀 미팅"));

        // TUESDAY
        create(new ScheduleRequest("물리 실험", DayOfWeek.TUESDAY,
                LocalTime.of(14, 0), LocalTime.of(15, 30),
                "실험실", "데이터 기록"));

        // WEDNESDAY
        create(new ScheduleRequest("화학", DayOfWeek.WEDNESDAY,
                LocalTime.of(8, 30), LocalTime.of(10, 0),
                "201호", ""));

        // THURSDAY
        create(new ScheduleRequest("코딩 스터디", DayOfWeek.THURSDAY,
                LocalTime.of(18, 0), LocalTime.of(20, 0),
                "온라인", "Spring Boot 실습"));

        // FRIDAY
        create(new ScheduleRequest("조깅", DayOfWeek.FRIDAY,
                LocalTime.of(6, 30), LocalTime.of(7, 30),
                "한강 공원", "아침 운동"));

        // SATURDAY
        create(new ScheduleRequest("음악 감상", DayOfWeek.SATURDAY,
                LocalTime.of(15, 0), LocalTime.of(16, 30),
                "거실", "새 앨범 듣기"));

        // SUNDAY
        create(new ScheduleRequest("등산", DayOfWeek.SUNDAY,
                LocalTime.of(7, 0), LocalTime.of(12, 0),
                "북한산", "친구들과 함께"));
    }



    public List<ScheduleEntry> findAll() {
        return new ArrayList<>(store.values());
    }

    // Optonal로 감싸서 반환 null 을 처리하기에 매우 용이함 ex) ScheduleController.getOne(id)
    // id 조회
    public Optional<ScheduleEntry> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
    // 일정 생성
    public ScheduleEntry create(ScheduleRequest req) {
        checkTimeConflict(req, null);
        Long id = seq.getAndIncrement();
        String color = generateColor(req.getTitle());
        ScheduleEntry entry = new ScheduleEntry(
                id,
                req.getTitle(),
                req.getDayOfWeek(),
                req.getStartTime(),
                req.getEndTime(),
                req.getLocation(),
                req.getMemo(),
                color
        );
        store.put(id, entry);
        return entry;
    }
    // 일정 수정
    public ScheduleEntry update(Long id, ScheduleRequest req) {
        if (!store.containsKey(id)) {
            throw new NoSuchElementException("No schedule for id=" + id);
        }
        checkTimeConflict(req, id);
        String color = generateColor(req.getTitle());
        ScheduleEntry entry = new ScheduleEntry(
                id,
                req.getTitle(),
                req.getDayOfWeek(),
                req.getStartTime(),
                req.getEndTime(),
                req.getLocation(),
                req.getMemo(),
                color
        );
        store.put(id, entry);
        return entry;
    }
    // 일정 삭제
    public void delete(Long id) {
        store.remove(id);
    }
    // 시간 충돌 검사
    private void checkTimeConflict(ScheduleRequest req, Long selfId) {
        for (ScheduleEntry other : store.values()) {
            // 자기자신은 검사 대상 제외 ( update )
            if (selfId != null && other.getId().equals(selfId)) continue;
            // 요일이 다르면 제외
            if (other.getDayOfWeek() != req.getDayOfWeek()) continue;
            // 시작시간과 끝시간 비교
            if (req.getStartTime().isBefore(other.getEndTime()) &&
                    req.getEndTime().isAfter(other.getStartTime())) {
                throw new IllegalArgumentException(
                        String.format("시간 충돌 [%s] %s~%s",
                                other.getTitle(),
                                other.getStartTime(),
                                other.getEndTime())
                );
            }
        }
    }
    /*
    제목에 따른 색깔지정
    generateColor(String title) 메서드는 일정 제목을 입력받아 “항상 같은” 16진수 RGB 색상 문자열(예: #a1b2c3)을 돌려주는 아주 단순하면서도 결정적인(Deterministic) 알고리즘입니다. 주요 설계 포인트를 하나씩 풀어보면 다음과 같습니다.
    왜 해시 함수를 쓰나
    • 랜덤(random)으로 뽑으면 매번 결과가 달라져서 UI가 일관되지 않음
    • 미리 정의된 팔레트(palette)를 쓰면 제목 수만큼 색상이 부족하거나 수작업 관리가 번거로움
    → 해시 함수로 “제목 → 숫자(바이트 배열)” 매핑 후, 그중 일부를 색상 값으로 사용하면
    – 같은 입력은 항상 같은 출력
    – 다른 입력은 거의 균등 분포된 색상
    을 얻을 수 있습니다.

    MD5 해시 사용 이유
    • Java 표준에 내장된 MessageDigest.getInstance("MD5") 가 쉽고 빠름
    • MD5 는 128비트(16바이트) 해시 출력
    • 충돌 가능성(서로 다른 제목이 같은 해시를 가질 확률)은 매우 낮음(실제 쓰는 문자열 범위에서 무시 가능)

      제목 → MD5 해시 → 첫 3바이트 → RGB → #RRGGBB
    */
    public String generateColor(String title) {
        try {
            // 1) MD5 해시 객체
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 2) 제목을 UTF-8 바이트로 변환 후 해시
            byte[] hash = md.digest(title.getBytes(StandardCharsets.UTF_8));
            // 3) 해시의 첫 3바이트를 각각 0~255 정수로 변환하여 R,G,B로 사용
            // 4) #RRGGBB 형식의 16진수 문자열로 포맷
            return String.format("#%02x%02x%02x",
                    hash[0] & 0xff,
                    hash[1] & 0xff,
                    hash[2] & 0xff);
        } catch (Exception e) {
            // MD5 알고리즘이 없거나 예외 발생 시 기본 회색 리턴
            return "#cccccc";
        }
    }
    /*
      Slot 내부클래스를 만들어 30분 슬롯 생성
      요일 별 새로운 슬롯 리스트 생성
      일정 추가
    */
    // 주간 시간표 ( 30분 단위)
    public Map<DayOfWeek, List<Slot>> getWeeklyTable() {
        List<LocalTime> times = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            times.add(LocalTime.MIN.plusMinutes(30L * i));
        }
        Map<DayOfWeek, List<Slot>> table = new LinkedHashMap<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            List<Slot> slots = new ArrayList<>();
            for (LocalTime t : times) {
                Slot s = new Slot();
                s.setTime(t);
                for (ScheduleEntry e : store.values()) {
                    if ( e.getDayOfWeek() == d &&
                         !t.isBefore(e.getStartTime()) &&
                         t.isBefore(e.getEndTime()))
                    {
                        s.setEntryId(e.getId());
                        s.setTitle(e.getTitle());
                        s.setColor(e.getColor());
                        break;
                    }
                }
                slots.add(s);
            }
            table.put(d, slots);
        }
        return table;
    }
    // 통계
    public Stats getStats() {
        List<ScheduleEntry> all = findAll();
        int total = all.size();
        // 요일별 일정 개수
        Map<DayOfWeek, Long> countByDay = Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(
                        d -> d,
                        d -> all.stream()
                                .filter(e -> e.getDayOfWeek() == d)
                                .count(),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
        // 요일별 총 분
        Map<DayOfWeek, Long> minutesByDay = Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toMap(
                        d -> d,
                        d -> all.stream()
                                .filter(e -> e.getDayOfWeek() == d)
                                .mapToLong(e -> Duration.between(e.getStartTime(), e.getEndTime()).toMinutes())
                                .sum(),
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
        // 토탈 분
        long totalMin = minutesByDay.values().stream().mapToLong(Long::longValue).sum();
        // 평균
        long avg = total == 0 ? 0 : totalMin / total;

        Stats s = new Stats();
        s.setTotalSchedules(total);
        s.setSchedulesByDay(countByDay);
        s.setMinutesByDay(minutesByDay);
        s.setAverageDurationMinutes(avg);
        return s;
    }

    @Data
    public static class Slot {
        private LocalTime time;
        private Long entryId;
        private String title;
        private String color;
    }

    @Data
    public static class Stats {
        private int totalSchedules;
        private Map<DayOfWeek, Long> schedulesByDay;
        private Map<DayOfWeek, Long> minutesByDay;
        private long averageDurationMinutes;
    }
}