package com.example.bootdailymission.springBasic.task04;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CalculatorMain {
    public static void main(String[] args) {
        // 이렇게 컨텍스트를 따로 띄우면 Boot의 자동설이 적용되지 않으므로 AppConfig @EnableAspectJAutoProxy 을 달아줘야함
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        CalculatorService service = context.getBean(CalculatorService.class);
        service.performCalculations();

    }
}