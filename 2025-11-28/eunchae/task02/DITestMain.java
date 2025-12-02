package com.example.spring_basic.task02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.GenericXmlApplicationContext;

@SpringBootApplication
public class DITestMain implements CommandLineRunner {
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(DITestMain.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        System.out.println("XML 기반 DI 테스트");
        GenericXmlApplicationContext xmlContext = new GenericXmlApplicationContext("applicationContext.xml");
        UserService userServiceXml = (UserService) xmlContext.getBean("userServiceXml");
        System.out.println("결과: " + userServiceXml.getUserName("XML User"));
        xmlContext.close();

        System.out.println("애너테이션 기반 DI 테스트");
        System.out.println("결과: " + userService.getUserName("Annotation User"));

        System.out.println("실행 결과 - XML 기반 DI와 애너테이션 기반 DI 모두 정상 동작");

    }
}
