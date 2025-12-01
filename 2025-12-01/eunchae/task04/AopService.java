package com.example.spring_basic.task04;

import org.springframework.stereotype.Service;

@Service
public class AopService {
    public String execute(String data) throws InterruptedException {
        System.out.println("핵심 로직: " + data);
        Thread.sleep(200);
        return data;
    }
}
