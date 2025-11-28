package com.example.bootdailymission;

import com.example.bootdailymission.springBasic.Task01;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = "com.example.bootdailymission.springBasic")
public class BootDailyMissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootDailyMissionApplication.class, args);
    }
}
