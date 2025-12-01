package org.mycompany.test.interfaceDi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor //생성자 주입
public class InterfaceDiApplication implements CommandLineRunner {

    private final CalculatorService service;

    public static void main(String[] args) {
        SpringApplication.run(InterfaceDiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("인터페이스 DI ---------------------");
        service.run();
    }
    //이 클래스는 구현체를 인터페이스을 상속받게 하여 자유롭게 바꿀수있게 했음
    //의존성은 생성자 주입을 사용해서 넣었음.
}
