//package com.example.trustcare.logging;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import java.util.UUID;
//
//@Aspect
//@Component
//public class LoggingAspect {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//    @Around("execution(* com.example.trustcare.controller..*(..)) || " +
//            "execution(* com.example.trustcare.service.*(..)) || " +
//            "execution(* com.example.trustcare.repository..*(..)) || " +
//            "execution(* com.example.trustcare.security..*(..))")
//    public Object logWithMDC(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            String requestId = UUID.randomUUID().toString();
//            MDC.put("requestId", requestId);
//            String className = joinPoint.getTarget().getClass().getSimpleName();
//            String methodName = joinPoint.getSignature().getName();
//            MDC.put("controller", className);
//            MDC.put("method", methodName);
//            logger.info(LogUtils.info("Entering " + className + "." + methodName));
//            Object result = joinPoint.proceed();
//            logger.info(LogUtils.success("Exiting " + className + "." + methodName));
//            return result;
//        } finally {
//            MDC.clear();
//        }
//    }
//}