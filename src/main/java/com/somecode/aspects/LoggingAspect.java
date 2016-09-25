package com.somecode.aspects;

import com.somecode.dao.PacketAppDaoImpl;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.somecode.helper.Helper.getMessage;

/**
 * Created by alexc_000 on 2016-09-24.
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = Logger.getLogger(PacketAppDaoImpl.class);
    private static final String BEFORE_DAO_LOGGING = "loggingAspect.beforeDaoLogging";
    private static final String BEFORE_LOGGING = "loggingAspect.beforeLogging";
    private static final String AFTER_LOGGING = "loggingAspect.afterLogging";

    @Pointcut("execution(* com.somecode.dao.PacketAppDao*.load*(..)) " +
            "|| execution(* com.somecode.dao.PacketAppDao*.delete*(..)) " +
            "|| execution(* com.somecode.dao.PacketAppDao*.update*(..)) " +
            "|| execution(* com.somecode.dao.PacketAppDao*.add*(..)) " +
            "|| execution(* com.somecode.dao.PacketAppDao*.getUser*(..)) ")
    public void basicDaoMethods() {
    }

    @Around("basicDaoMethods()")
    public Object daoLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        LOGGER.info(getMessage(BEFORE_DAO_LOGGING, new Object[]{pjp.getSignature().getName(), pjp.getArgs()}));
        Object retVal = pjp.proceed();
        LOGGER.info(getMessage(AFTER_LOGGING, new Object[]{ClassType.DAO.toString(), pjp.getSignature().getName()}));
        return retVal;
    }

    @Pointcut("execution(* com.somecode.controller..*.*(..))")
    public void allControllerMethods() {
    }

    @Around("allControllerMethods()")
    public Object controllerLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info(getMessage(BEFORE_LOGGING,
                new Object[]{ClassType.CONTROLLER.toString(), pjp.getSignature().getName()}));
        Object retVal = pjp.proceed();
        LOGGER.info(getMessage(AFTER_LOGGING,
                new Object[]{ClassType.CONTROLLER.toString(), pjp.getSignature().getName()}));
        return retVal;
    }

    @Pointcut("execution(* com.somecode.service..*.*(..))")
    public void allServiceMethods() {
    }

    @Around("allServiceMethods()")
    public Object serviceLoggingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info(getMessage(BEFORE_LOGGING,
                new Object[]{ClassType.SERVICE.toString(), pjp.getSignature().getName()}));
        Object retVal = pjp.proceed();
        LOGGER.info(getMessage(AFTER_LOGGING,
                new Object[]{ClassType.SERVICE.toString(), pjp.getSignature().getName()}));
        return retVal;
    }
}
