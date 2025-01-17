package com.example.userservice.AspectLogger;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

    @Around("@annotation(loggable)")
    public Object logMethod(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String message = loggable.message();
        Object[] args = joinPoint.getArgs();
        String level = loggable.level().toUpperCase();

        logMessage(message, level, methodName, args);
        return joinPoint.proceed();
    }

    private void logMessage(String message, String level, String methodName, Object... args) {
        switch (level.toLowerCase()) {
            case "debug":
                log.debug("Method {} {} with parameters{}", methodName, message, Arrays.toString(args));
                break;
            case "info":
                log.info("Method {} {} with parameters{}", methodName, message, Arrays.toString(args));
                break;
            case "warn":
                log.warn("Method {} {} with parameters{}", methodName, message, Arrays.toString(args));
                break;
            case "error":
                log.error("Method {} {} with parameters{}", methodName, message, Arrays.toString(args));
                break;
        }
    }
}
