package com.example.spring_basic.task01;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class AppConfig {
    private String name;
    private String memo;
    private String developer;

}
