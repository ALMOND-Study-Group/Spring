package com.example.bootdailymission.springBasic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class Task01 {
    // @Value로 직접 주입 (app.test1.name, app.test1.message)
    @Value("${app.test1.name}")
    private String name;
    @Value("${app.test1.message}")
    private String message;

    // @ConfigurationProperties 바인딩 대상 (app.test2.*)
    private Test2 test2 = new Test2();

    @Getter
    @Setter
    public static class Test2 {
        private String name;
        private String message;
    }

    @PostConstruct
    public void init() {
        // @Value 주입된 값 출력
        System.out.println("======@Value======");
        System.out.println("app.test1.name = " + name);
        System.out.println("app.test1.message = " + message);
        // @ConfigurationProperties로 바인딩된 값 출력
        System.out.println("======@ConfigurationProperties======");
        System.out.println("app.test2.name = " + test2.getName());
        System.out.println("app.test2.message = " + test2.getMessage());
    }

}
