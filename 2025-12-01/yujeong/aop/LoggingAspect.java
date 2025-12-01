package com.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // 포인트컷: 어떤 메서드에 적용할지 지정
    @Pointcut("execution(* com.example.aop.Task.*(..))")
    public void serviceMethods() {}

    // 서비스 메서드 실행 전
    @Before("serviceMethods()")
    public void beforeLog(JoinPoint joinPoint) {
        log.info("▶ START: {} args={}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    // 서비스 메서드가 끝난 후 항상 실행
    @After("serviceMethods()")
    public void afterLog(JoinPoint joinPoint) {
        log.info("◀ After: {} args={}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    // 서비스 메서드 실행 후 (정상 종료)
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void afterReturningLog(JoinPoint joinPoint, Object result) {
        log.info("◀ AfterReturning: {} return={}",
                joinPoint.getSignature(), result);
    }

    // 예외 발생 시
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void afterThrowingLog(JoinPoint joinPoint, Exception ex) {
        log.error("💥 ERROR in {} message={}",
                joinPoint.getSignature(), ex.getMessage());
    }

    // 핵심 로직을 담을 수 있는 것
    @Around("serviceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 핵심 로직 실행
        long end = System.currentTimeMillis();
        long executionTime = end - start;
        log.info("[로직 실행 시간]: {} 이 로직이 {} ms 만에 실행 됐습니다!", joinPoint.getSignature(), executionTime);

        return result;
    }
}
