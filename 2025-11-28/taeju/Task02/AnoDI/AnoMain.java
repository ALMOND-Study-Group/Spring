package org.mycompany.test.AnoDI;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


public class AnoMain {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnoConfig.class);
        OutPrinter printer = context.getBean(OutPrinter.class);
        printer.print("애노테이션 빈 등록");
        
    }
}
