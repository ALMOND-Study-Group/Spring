package org.mycompany.test.DI;

public class OutPrinter {
    private final Printer printer;

    //생성자 주입
    public OutPrinter(Printer printer) {
        this.printer = printer;
    }

    public void print(String message){
        printer.print(message);
    }

}
