package com.example.spring_basic.task01;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.spring_basic")
public class PropertiesMain {
    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.application.name}")
    private String applicationName;

    private final AppConfig appConfig;

    public PropertiesMain(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
    public static void main(String[] args) {
        SpringApplication.run(PropertiesMain.class, args);
    }

   @PostConstruct
    public void check() {
        System.out.println("Value 확인");
        System.out.println("App Name: " + applicationName);
        System.out.println("Server Port: " + serverPort);

        System.out.println("\nConfigurationProperties 확인");
        System.out.println("AppConfig: " + appConfig);

        System.out.println("\nproperties 파일의 설정 값이 정상적으로 주입됨 확인");
    }


}
