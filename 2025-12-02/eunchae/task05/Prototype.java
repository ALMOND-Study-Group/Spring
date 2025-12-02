package com.example.spring_basic.task05;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Prototype {
    public Prototype() {
    }

    @PostConstruct
    public void init() {
        System.out.println("초기화 콜백");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("소멸 전 콜백");
    }
}
