package com.example.di.xml;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlDITest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        MessagePrinter printer = context.getBean("printer", MessagePrinter.class);
        printer.printMessage();

        context.close();
    }
}
