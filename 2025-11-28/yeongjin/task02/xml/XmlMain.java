package com.example.bootdailymission.springBasic.task02.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlMain {
    public static void main(String[] args) {
        // XML 설정 파일로 컨텍스트 생성
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        NameClient client = context.getBean("nameClient", NameClient.class);
        client.printName();
    }
}
