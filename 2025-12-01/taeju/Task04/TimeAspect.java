package org.mycompany.test.AOP;

import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.annotation.Target;

@Aspect
@Component
public class TimeAspect {

    @Around("execution(* org.mycompany.test.AOP..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return pjp.proceed(); // 실제 메서드 실행
        } finally {
            stopWatch.stop();
            String className = pjp.getSignature().getDeclaringType().getSimpleName();
            String methodName = pjp.getSignature().getName();

            System.out.printf(
                    "[실행시간 측정] %s.%s() 실행 시간 = %d ms%n",
                    className, methodName, stopWatch.getTotalTimeMillis()
            );
        }
    }
}
