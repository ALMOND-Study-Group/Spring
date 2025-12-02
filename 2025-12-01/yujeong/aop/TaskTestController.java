package com.example.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskTestController {

    private final Task task;

    @GetMapping("/test")
    public String test() {
        return task.timeTask();
    }

    @GetMapping("/test2")
    public void test2() {
        task.registerMember("유정", -1);
    }
}
