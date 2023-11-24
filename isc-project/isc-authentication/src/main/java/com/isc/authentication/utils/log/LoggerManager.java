package com.isc.authentication.utils.log;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.isc.authentication.utils.PrivacyUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class LoggerManager {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    String param;

    @Autowired
    PrivacyUtils privacyUtils;


    public void doLog(Level level, String param, JoinPoint joinPoint, String nameLogger, Boolean shouldIPutStarttag, String user) throws JsonProcessingException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String method = joinPoint.getSignature().getName();
        String path = joinPoint.getTarget().getClass().getName() + "." + method;
        String message;
        String uuid = UUID.randomUUID().toString();

        LoggerManager.setSessionParamsLog("methodName", path);
        LoggerManager.setSessionParamsLog("startTimestamp", String.valueOf(timestamp));
        LoggerManager.setSessionParamsLog("user", user);
        LoggerManager.setSessionParamsLog("uuid", uuid);
        //LoggerManager.setSessionParamsLog("chiamante",joinPoint.getSignature().getName());

        Logger logger = LogManager.getLogger(nameLogger);

        if (shouldIPutStarttag) {
            setSessionParamsLog("startEndTag", "[START]");
            message = "METHOD: " + method + ", REQUEST: " + param;
        } else {
            message = "METHOD: " + method + ", RESPONSE: " + param;
            setSessionParamsLog("startEndTag", "[OK]");
        }
        logger.log(level, message);

        ThreadContext.remove("startEndTag");

    }

    public static void setSessionParamsLog(String keyLog, String valueLog) {
        ThreadContext.put(keyLog, valueLog);
    }

    public void doException(String e, String callerClass) {
        Logger logger = LogManager.getLogger(callerClass);
        String message = "[KO] " + e;
        logger.log(Level.getLevel("ERROR"), message);
        ThreadContext.remove("startEndTag");
    }

    public void publishCreateLogEventBefore(final MessageLogBefore message) {
        CreateLog customSpringEvent = new CreateLog(this, message, null, 0);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishCreateLogEventAfter(final MessageLogAfter message) {
        CreateLog customSpringEvent = new CreateLog(this, null, message, 1);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishCreateLogEventAfterThrow(final MessageLogAfter message) {
        CreateLog customSpringEvent = new CreateLog(this, null, message, 2);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    @Async
    @EventListener(condition = "#log.id == 0")
    void handleAsyncEventBefore(CreateLog log) throws JsonProcessingException {
        JoinPoint joinPoint = log.getMessageBefore().getJoinPoint();
        String user = log.getMessageBefore().getUser();
        param = new ObjectMapper().writer().writeValueAsString(privacyUtils.hideSensitveFields(joinPoint.getArgs()));
        doLog(Level.getLevel("AUDIT"), param, joinPoint, "authentication_audit", true, user);
    }


    @Async
    @EventListener(condition = "#log.id == 1")
    void handleAsyncEventAfter(CreateLog log) throws JsonProcessingException {
        JoinPoint joinPoint = log.getMessageAfter().getJoinPoint();
        Object response = log.getMessageAfter().getResponse();
        String user = log.getMessageAfter().getUser();
        param = new ObjectMapper().writer().writeValueAsString(response);
        doLog(Level.getLevel("AUDIT"), param, joinPoint, "authentication_audit", false, user);
    }

    @Async
    @EventListener(condition = "#log.id == 2")
    void handleAsyncEventAfterThrow(CreateLog log) throws JsonProcessingException {
        JoinPoint joinPoint = log.getMessageAfter().getJoinPoint();
        param = new ObjectMapper().registerModule(new JavaTimeModule()).writer().writeValueAsString(privacyUtils.hideSensitveFields(joinPoint.getArgs()));
        doException(param, joinPoint.getSignature().getName());
    }


}
