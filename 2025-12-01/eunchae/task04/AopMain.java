package com.example.spring_basic.task04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopMain implements CommandLineRunner {

    @Autowired
    private AopService aopService;

    public static void main(String[] args) {
        SpringApplication.run(AopMain.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        String result = aopService.execute("test");
        System.out.println("최종 결과: " + result);
    }
}