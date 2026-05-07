package com.internship.tool.config;

import com.internship.tool.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class AuditAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Autowired
    private AuditLogService auditLogService;

    @Around("execution(* com.internship.tool.service.*.create*(..)) || " +
            "execution(* com.internship.tool.service.*.update*(..)) || " +
            "execution(* com.internship.tool.service.*.delete*(..))")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Audit Start: {}", methodName);

        Object result = joinPoint.proceed();

        auditLogService.saveAudit(
                methodName,
                "Vendor",
                "old json",
                "new json"
        );

        logger.info("Audit Success: {}", methodName);
        return result;
    }
}