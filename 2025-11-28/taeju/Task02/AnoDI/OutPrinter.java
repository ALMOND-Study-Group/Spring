package org.mycompany.test.AnoDI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OutPrinter {
    private final Printer printer;

    //생성자 주입
    @Autowired
    public OutPrinter(@Qualifier("consolePrinter") Printer printer) {
        this.printer = printer;
    }

    public void print(String message){
        printer.print(message);
    }

}
