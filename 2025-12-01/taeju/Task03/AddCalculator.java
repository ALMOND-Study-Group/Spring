package org.mycompany.test.interfaceDi;

public class AddCalculator implements Calculator {
    @Override
    public int calculate(int a, int b) {
        return a + b;
    }
}
