package org.mycompany.test.interfaceDi;

public class Subtract implements Calculator {
    @Override
    public int calculate(int a, int b) {
        return a - b;
    }
}
