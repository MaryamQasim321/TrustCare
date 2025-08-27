package com.example.trustcare.Logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class LoggingAspects {

    @Around("execution(* com.example.trustcare.controller..*(..))")
    public Object logWithMDC(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // Generate unique requestId for every request
            String requestId = UUID.randomUUID().toString();
            MDC.put("requestId", requestId);

            // Get controller + method name
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
            MDC.put("controller", className);
            MDC.put("method", methodName);

            // Log before execution
            System.out.println(LogUtils.info("Entering " + className + "." + methodName));

            // Proceed with method execution
            Object result = joinPoint.proceed();

            // Log after execution
            System.out.println(LogUtils.success("Exiting " + className + "." + methodName));

            return result;
        } finally {
            MDC.clear(); // clear to avoid memory leaks (important in multi-threaded apps)
        }
    }
}
