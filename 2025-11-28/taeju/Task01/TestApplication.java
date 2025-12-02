package org.mycompany.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.name}")
    private String dbName;

    @Value("${spring.datasource.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    //@ConfigurationProperties을 검증하기 위해서
    //생성자 주입,
    @Autowired
    DemoApplication demoApplication;

    /*public TestApplication(DemoApplication demoApplication) {
        this.demoApplication = demoApplication;
    }
*/

    @Override
    public void run(String... args) throws Exception {
        System.out.println("서버가 실행됨니다.");
        System.out.println(url);
        System.out.println(dbName);
        System.out.println(password);
        
        //@ConfigurationProperties을 검증하기 위해서
        System.out.println(demoApplication.toString());

    }



}
