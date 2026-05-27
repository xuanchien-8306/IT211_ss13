package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.demo.controller..*(..))")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        logger.info(
                "Controller method called: {}",
                joinPoint.getSignature().getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.demo.service..*(..))",
            returning = "result"
    )
    public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
        logger.info(
                "Service method {} returned: {}",
                joinPoint.getSignature().getName(),
                result
        );
    }

    @Around("execution(* com.example.demo.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        logger.info(
                "Controller method {} executed in {} ms",
                joinPoint.getSignature().getName(),
                duration
        );

        return result;
    }
}