package com.example.bootdailymission.springBasic.task05;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class SingletonBean {
    @PostConstruct
    public void init() {
        System.out.println("SingletonBean @PostConstruct 호출: " + this);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("SingletonBean @PreDestroy 호출: " + this);
    }
}