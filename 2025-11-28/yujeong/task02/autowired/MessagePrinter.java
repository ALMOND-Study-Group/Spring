package com.example.di.autowired;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
// @RequiredArgsConstructor -> 대신할 수 있음!
public class MessagePrinter {

    private final MessageService messageService;

    // 자동으로 MessageService 생성자 주입
    @Autowired
    public MessagePrinter(MessageService messageService) {
        this.messageService = messageService;
    }

    public void printMessage() {
        System.out.println(messageService.getMessage());
    }
}
