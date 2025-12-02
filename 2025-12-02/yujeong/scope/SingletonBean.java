package com.example.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class SingletonBean {

    public SingletonBean() {
        System.out.println("[Singleton] 생성자 호출");
    }

    @PostConstruct
    public void init() {
        System.out.println("[Singleton] @PostConstruct 실행");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[Singleton] @PreDestroy 실행");
    }
}