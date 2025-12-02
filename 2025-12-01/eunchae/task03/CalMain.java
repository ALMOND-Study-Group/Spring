package com.example.spring_basic.task03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalMain implements CommandLineRunner {
    private final Calculator calculator;

    @Autowired
    public CalMain(@Qualifier("subCal") Calculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(CalMain.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        int result = calculator.calculate(3, 5);
        System.out.println("결과: " + result);
        System.out.println("\n실행 결과 요약 : 인터페이스 기반 DI로 구현체 교체 가능 확인");

    }
}
