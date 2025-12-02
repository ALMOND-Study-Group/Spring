package com.example.bootdailymission.springBasic.task05;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("\n=== Singleton Bean 조회 2번 ===");
        SingletonBean s1 = context.getBean(SingletonBean.class);
        SingletonBean s2 = context.getBean(SingletonBean.class);
        System.out.println("singleton 1 = " + s1);
        System.out.println("singleton 2 = " + s2);

        System.out.println("\n=== Prototype Bean 조회 2번 ===");
        PrototypeBean p1 = context.getBean(PrototypeBean.class);
        PrototypeBean p2 = context.getBean(PrototypeBean.class);
        System.out.println("prototype 1 = " + p1);
        System.out.println("prototype 2 = " + p2);

        System.out.println("\n=== Prototype Bean 수동 종료 ===");
        // BeanFactory에게 파괴를 위임 (ConfigurableBeanFactory.destroyBean)
        ConfigurableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.destroyBean("PrototypeBean", p1);
        System.out.println("prototype 1 = " + p1);
        System.out.println("\n=== 컨테이너 종료 ===");

        context.close();
    }
}
