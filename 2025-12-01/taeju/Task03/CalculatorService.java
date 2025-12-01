package org.mycompany.test.interfaceDi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    //private final Calculator calculator;
    private final Calculator calculator;

    /**
     * public CalculatorService(Calculator calculator) {
     *         this.calculator = calculator;
     *     }
     */
    public void run(){
        calculator.calculate(10,5);
        System.out.println("계산결과 = "+ calculator.calculate(10,5));
        System.out.println(calculator.getClass().getSimpleName());
    }
}
