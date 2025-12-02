package com.example.spring_basic.task05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LifecycleMain implements CommandLineRunner {
    @Autowired
    private ApplicationContext ac;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LifecycleMain.class, args);
        context.close();
    }

    @Override
    public void run(String[] args) throws Exception {
        System.out.println("<싱글톤 테스트>");
        Singleton s1 = ac.getBean(Singleton.class);
        Singleton s2 = ac.getBean(Singleton.class);
        System.out.println(s1 == s2);

        System.out.println("<프로토타입 테스트>");
        Prototype p1 = ac.getBean(Prototype.class);
        Prototype p2 = ac.getBean(Prototype.class);
        System.out.println(p1 == p2);



    }
}



