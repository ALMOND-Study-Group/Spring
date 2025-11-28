package org.mycompany.test.DI;

public class ConsolePrinter implements Printer {
    @Override
    public void print(String message) {
        System.out.println("[콘솔 출력]"+message);
    }
}
