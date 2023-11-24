package com.isc.authentication.utils.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class AspectUtil {

    @Autowired
    LoggerManager loggerManager;

    @Pointcut("execution(* com.isc.authentication.controller.*.*(..))")
    public void point() {

    }

    @Before("point()")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        loggerManager.publishCreateLogEventBefore(new MessageLogBefore(joinPoint, SecurityContextHolder.getContext().getAuthentication().getName()));
    }


    @AfterReturning(pointcut = "point()", returning = "response")
    public void logAfter(JoinPoint joinPoint, Object response) throws JsonProcessingException {
        loggerManager.publishCreateLogEventAfter(new MessageLogAfter(joinPoint, response, SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @AfterThrowing(pointcut = "point()",
            throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) throws ClassNotFoundException {
        loggerManager.publishCreateLogEventAfterThrow(new MessageLogAfter(joinPoint, error.getMessage(), null));

    }

/*    public void controlloRequest(String args){
        if(args instanceof )
    }
*/
}
