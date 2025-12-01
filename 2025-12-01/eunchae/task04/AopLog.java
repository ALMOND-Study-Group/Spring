package com.example.spring_basic.task04;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopLog {

    @Around("execution(* com.example.spring_basic.task04..*.*(..))")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[Aspect] " + methodName + " 실행, 시간: " + duration + "ms");
        }
        return result;
    }
}