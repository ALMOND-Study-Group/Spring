package com.example.aop;

import org.springframework.stereotype.Service;

@Service
public class Task {

    public String timeTask() {
        try {
            Thread.sleep(500); // 0.5초 걸리는 로직
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //throw new RuntimeException("[예외] 강제 예외 발생!");
        return "[Task] 완료";
    }

    public void registerMember(String name, int age) {
        System.out.println("회원 등록 로직 시작");

        if (age <= 0) {
            // @AfterThrowing이 잡을 예외
            throw new IllegalArgumentException("나이는 0보다 커야 합니다. 입력값=" + age);
        }

        // 여기서는 그냥 성공했다고 가정
        System.out.println("회원 등록 성공: name=" + name + ", age=" + age);
    }
}