package com.example.di.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CalculatorUser {

    private final Calculator calculator;

    @Autowired
    public CalculatorUser(@Qualifier("addCalculator") Calculator calculator) { // 사용할 구현체 선택
        this.calculator = calculator;
    }

    public void execute() {
        System.out.println("[Add]: " + calculator.add(5, 3));
        System.out.println("[Subtract]: " + calculator.subtract(5, 3));
    }
}