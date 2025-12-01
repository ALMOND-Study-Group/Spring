package com.example.di.calculator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CalculatorAnnotationDITest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.example.di");

        CalculatorUser user = context.getBean(CalculatorUser.class);
        user.execute();

        context.close();
    }
}
