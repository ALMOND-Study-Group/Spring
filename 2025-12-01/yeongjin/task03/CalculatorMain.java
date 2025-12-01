package com.example.bootdailymission.springBasic.task03;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CalculatorMain {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        CalculatorService service = context.getBean(CalculatorService.class);
        service.performCalculations();

    }
}