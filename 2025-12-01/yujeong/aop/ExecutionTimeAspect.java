package com.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    // com.example.service 패키지의 모든 메서드 실행 시 적용
    @Around("execution(* com.example.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 핵심 로직 실행
        long end = System.currentTimeMillis();
        long executionTime = end - start;
        log.info("[로직 실행 시간]: {} 이 로직이 {} ms 만에 실행 됐습니다!", joinPoint.getSignature(), executionTime);

        return result;
    }
}
