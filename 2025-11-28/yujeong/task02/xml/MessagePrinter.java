package com.example.di.xml;

public class MessagePrinter {

    private final MessageService messageService; // 의존성

    public MessagePrinter(MessageService messageService) {
        this.messageService = messageService;
    }

    public void printMessage() {
        System.out.println(messageService.getMessage());
    }
}