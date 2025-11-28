package com.example.configuration.controller;

import com.example.configuration.config.CustomProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigTestController {

    private final CustomProperties customProperties;

    @GetMapping("/config")
    public String configTest() {
        return "Title: " + customProperties.getTitle() +
                ", Version: " + customProperties.getVersion() +
                ", Author: " + customProperties.getAuthor() +
                ", Welcome: " + customProperties.getMessage().getWelcome();
    }
}
