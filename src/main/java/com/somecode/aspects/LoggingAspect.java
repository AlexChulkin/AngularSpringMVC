package com.somecode.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.somecode.utils.Utils.getMessage;

/**
 * Created by alexc_000 on 2016-09-24.
 */
@Aspect
@Component
@Log4j
public class LoggingAspect {
    private static final String BEFORE_LOGGING = "loggingAspect.beforeLogging";
    private static final String AFTER_LOGGING = "loggingAspect.afterLogging";

    @Pointcut("execution(public * com.somecode.dao.PacketAppDao*.*(..)) " )
    public void basicDaoMethods() {
    }

    @Around("basicDaoMethods()")
    public Object daoLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.DAO, pjp);
    }

    @Pointcut("execution(* com.somecode.controller..*.*(..))")
    public void allControllerMethods() {
    }

    @Around("allControllerMethods()")
    public Object controllerLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.CONTROLLER, pjp);
    }

    @Pointcut("execution(* com.somecode.service..*.*(..))")
    public void allServiceMethods() {
    }

    @Around("allServiceMethods()")
    public Object serviceLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.SERVICE, pjp);
    }

    private Object universalLoggingAroundAdvice(ClassType classType, ProceedingJoinPoint pjp) throws Throwable {
        String moduleName = classType.toString();
        String signature = pjp.getSignature().toString();
        log.debug(getMessage(BEFORE_LOGGING, new Object[]{moduleName, signature}));
        Object retVal = pjp.proceed();
        log.debug(getMessage(AFTER_LOGGING, new Object[]{moduleName, signature}));
        return retVal;
    }
}
