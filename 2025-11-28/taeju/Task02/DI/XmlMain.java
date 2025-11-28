package org.mycompany.test.DI;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("printer.xml");

        OutPrinter printer = context.getBean("OutPrinter", OutPrinter.class);
        printer.print("heloo whod");

        ((ClassPathXmlApplicationContext)context).close();

    }
}
