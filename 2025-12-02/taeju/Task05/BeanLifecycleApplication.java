package org.mycompany.test.BeanCycle;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor //생성자 주입
public class BeanLifecycleApplication implements CommandLineRunner {

    private final ApplicationContext applicationContext; //스프링컨테이너, 여기에서 getbeans할려고
    private final SingletonBean singletonBean;

    public static void main(String[] args) {
        SpringApplication.run(BeanLifecycleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== 싱글톤 테스트 시작 ===");
        SingletonBean s1 = applicationContext.getBean(SingletonBean.class);
        SingletonBean s2 = applicationContext.getBean(SingletonBean.class);
        s1.sayHello();
        s2.sayHello();
        System.out.println("s1 == s2 ? " + (s1 == s2));  // true → 같은 객체!

        System.out.println("\n=== 프로토타입 테스트 시작 ===");
        PrototypeBean p1 = applicationContext.getBean(PrototypeBean.class);
        PrototypeBean p2 = applicationContext.getBean(PrototypeBean.class);
        p1.sayHello();
        p2.sayHello();
        System.out.println("p1 == p2 ? " + (p1 == p2)); // false → 다른 객체!

        s1.destroy();
        s1.sayHello();

        s2.destroy();
        p1.destroy();
        p2.destroy();
        p1.sayHello();

    }
}
