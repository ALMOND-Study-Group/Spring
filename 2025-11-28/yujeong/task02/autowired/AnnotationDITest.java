package com.example.di.autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationDITest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.example.di");

        MessagePrinter printer = context.getBean(MessagePrinter.class);
        printer.printMessage();
    }
}
