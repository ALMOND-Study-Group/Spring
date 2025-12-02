package com.example.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean {

    public PrototypeBean() {
        System.out.println("[Prototype] 생성자 호출");
    }

    @PostConstruct
    public void init() {
        System.out.println("[Prototype] @PostConstruct 실행");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[Prototype] @PreDestroy 실행 - (호출 안 됨)");
    }
}