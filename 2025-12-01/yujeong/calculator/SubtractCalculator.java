package com.example.di.calculator;

import org.springframework.stereotype.Component;

@Component
public class SubtractCalculator implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
