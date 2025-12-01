package com.example.bootdailymission.springBasic.task04;

import org.springframework.stereotype.Component;


@Component("subtractCalculator")
public class SubtractCalculator implements Calculator {

    @Override
    public int add(int a, int b) {
        System.out.println("SubtractCalculator");
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("SubtractCalculator");
        int result = a - b;
        System.out.println(a + " - " + b + " = " + result);
        return result;
    }
}