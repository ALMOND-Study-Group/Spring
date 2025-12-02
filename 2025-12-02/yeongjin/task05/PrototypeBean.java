package com.example.bootdailymission.springBasic.task05;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class PrototypeBean {
    @PostConstruct
    public void init() {
        System.out.println("PrototypeBean @PostConstruct 호출: " + this);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PrototypeBean @PreDestroy 호출: " + this);
    }
}

