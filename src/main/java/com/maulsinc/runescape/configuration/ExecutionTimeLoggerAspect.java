package com.maulsinc.runescape.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLoggerAspect {
    @Around("@annotation(ExecutionTimeLogger)")
    public Object executionTimeLogger(ProceedingJoinPoint joinPoint) {
        try {
            long startTime = System.currentTimeMillis();
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("{} method was executed in {}", joinPoint.getSignature().getName(), getTimeFormat(executionTime));

            return proceed;
        } catch (Throwable e) {
            log.error("There was an error while calculating method execution time for {}", joinPoint.getSignature(), e);
            return null;
        }
    }

    private String getTimeFormat(final long totalMilliseconds) {
        long milliseconds = totalMilliseconds % 1000;
        long second = (totalMilliseconds / 1000) % 60;
        long minute = (totalMilliseconds / (1000 * 60)) % 60;

        return String.format("%s:%s.%s", minute, second, milliseconds);
    }
}
