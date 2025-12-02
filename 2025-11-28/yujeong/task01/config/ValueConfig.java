package com.example.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueConfig {

    @Value("${custom.title}")
    private String title;

    @Value("${custom.version}")
    private double version;

    @Value("${custom.author}")
    private String author;

    @Value("${custom.message.welcome}")
    private String welcomeMessage;

    public void printValues() {
        System.out.println("Title: " + title);
        System.out.println("Version: " + version);
        System.out.println("Author: " + author);
        System.out.println("Welcome: " + welcomeMessage);
    }
}
