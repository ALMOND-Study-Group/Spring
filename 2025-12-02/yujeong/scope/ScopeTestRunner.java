package com.example.scope;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ScopeTestRunner implements CommandLineRunner {

    private final ObjectProvider<SingletonBean> singletonProvider;
    private final ObjectProvider<PrototypeBean> prototypeProvider;

    public ScopeTestRunner(ObjectProvider<SingletonBean> singletonProvider,
                           ObjectProvider<PrototypeBean> prototypeProvider) {
        this.singletonProvider = singletonProvider;
        this.prototypeProvider = prototypeProvider;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== 싱글톤 빈 요청 2번 ===");
        SingletonBean s1 = singletonProvider.getObject();
        SingletonBean s2 = singletonProvider.getObject();
        System.out.println("싱글톤 동일 객체? " + (s1 == s2));

        System.out.println("\n=== 프로토타입 빈 요청 2번 ===");
        PrototypeBean p1 = prototypeProvider.getObject();
        PrototypeBean p2 = prototypeProvider.getObject();
        System.out.println("프로토타입 동일 객체? " + (p1 == p2));

        System.out.println("\n=== 프로그램 종료 ===\n");
    }
}