/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.aspects;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.somecode.utils.Utils.getMessage;
/**
 * The AOP module. Used for logging in service, restful controller and dao methods.
 */
@Aspect
@Component
@Log4j
public class LoggingAspect {
    /**
     * The message source constants
     */
    private static final String BEFORE_LOGGING = "loggingAspect.beforeLogging";
    private static final String AFTER_LOGGING = "loggingAspect.afterLogging";

    /**
     * Returns the public dao methods pointcut
     */
    @Pointcut("execution(public * com.somecode.dao.PacketAppDao*.*(..)) ")
    public void publicDaoMethods() {
    }

    /**
     * Returns the public dao methods around advice.
     *
     * @param pjp the corresponding joinpoint
     */
    @Around("publicDaoMethods()")
    public Object daoLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.DAO, pjp);
    }

    /**
     * Returns the restful controller methods pointcut
     */
    @Pointcut("execution(* com.somecode.controller.RestfulController.*(..))")
    public void allControllerMethods() {
    }

    /**
     * Returns the controller methods around advice.
     *
     * @param pjp the corresponding joinpoint
     */
    @Around("allControllerMethods()")
    public Object controllerLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.CONTROLLER, pjp);
    }

    /**
     * Returns the service methods pointcut
     */
    @Pointcut("execution(* com.somecode.controller.PacketAppService.*(..))")
    public void allServiceMethods() {
    }

    /**
     * Returns the service methods around advice.
     * @param pjp the corresponding joinpoint
     */
    @Around("allServiceMethods()")
    public Object serviceLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return universalLoggingAroundAdvice(ClassType.SERVICE, pjp);
    }

    /**
     * The private method responsible for building the simple advice with debug-level logging before and after the
     * method execution
     * @param classType the type of the module containing the target method
     * @param pjp the corresponding joinpoint
     */
    private Object universalLoggingAroundAdvice(ClassType classType, ProceedingJoinPoint pjp) throws Throwable {
        String moduleName = classType.toString();
        String signature = pjp.getSignature().toString();
        log.debug(getMessage(BEFORE_LOGGING, new Object[]{moduleName, signature}));
        Object retVal = pjp.proceed();
        log.debug(getMessage(AFTER_LOGGING, new Object[]{moduleName, signature}));
        return retVal;
    }
}
