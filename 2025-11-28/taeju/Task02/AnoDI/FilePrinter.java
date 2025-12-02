package org.mycompany.test.AnoDI;

import org.springframework.stereotype.Component;

@Component
public class FilePrinter implements Printer {
    @Override
    public void print(String message) {
        System.out.println("[파일 콘솔]"+message+" -->log.txt 저장됨");
    }
}
