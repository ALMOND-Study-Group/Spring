package com.example.bootdailymission.springBasic.task03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    private final Calculator calculator;

    // 생성자 주입 + @Qualifier로 구현체 선택
    // "addCalculator"를 "subtractCalculator"로 바꾸면 구현체 교체
    @Autowired
    public CalculatorService(@Qualifier("addCalculator") Calculator calculator) {
        this.calculator = calculator;
    }

    public void performCalculations() {
        int sum = calculator.add(10, 5);
        int diff = calculator.subtract(10, 5);

        System.out.println("sum: " + sum);
        System.out.println("diff: " + diff);
    }
}