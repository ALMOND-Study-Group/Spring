package com.example.spring_basic.task03;

import org.springframework.stereotype.Component;

@Component
public class SubCal implements Calculator{
    @Override
    public int calculate(int a, int b) {
        return a - b;
    }

}
