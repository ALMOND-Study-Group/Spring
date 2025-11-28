package com.example.bootdailymission.springBasic.task02.autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutowiredMain {
    public static void main(String[] args) {
        // Java Config로 컨텍스트 생성
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        NameClient client = context.getBean(NameClient.class);
        client.printName();
    }
}
