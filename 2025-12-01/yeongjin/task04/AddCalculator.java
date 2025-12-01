package com.example.bootdailymission.springBasic.task04;

import org.springframework.stereotype.Component;


@Component("addCalculator")
public class AddCalculator implements Calculator {

    @Override
    public int add(int a, int b) {
        System.out.println("AddCalculator");
        int result = a + b;
        System.out.println(a + " + " + b + " = " + result);
        return result;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("AddCalculator");
        return a - b;
    }
}