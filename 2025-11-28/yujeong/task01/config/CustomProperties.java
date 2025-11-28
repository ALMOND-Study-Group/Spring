package com.example.configuration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private String title;
    private double version;
    private String author;

    private Message message;

    @Getter
    @Setter
    public static class Message {
        private String welcome;
        private String description;
    }
}
