package com.example.bootdailymission.springBasic.task05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


public class AppConfig {
    @Bean
    // 기본 스코프가 singleton
    public SingletonBean SingletonBean() {
        return new SingletonBean();
    }

    @Bean
    @Scope("prototype")
    public PrototypeBean PrototypeBean() {
        return new PrototypeBean();
    }
}
