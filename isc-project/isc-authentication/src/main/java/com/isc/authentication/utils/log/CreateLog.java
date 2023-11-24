package com.isc.authentication.utils.log;

import org.springframework.context.ApplicationEvent;


public class CreateLog extends ApplicationEvent {

    public int id;

    public final MessageLogBefore messageBefore;

    public final MessageLogAfter messageAfter;

    public CreateLog(Object source, MessageLogBefore messageBefore, MessageLogAfter messageAfter, int id) {
        super(source);
        this.messageBefore = messageBefore;
        this.messageAfter = messageAfter;
        this.id = id;
    }

    public MessageLogBefore getMessageBefore() {
        return messageBefore;
    }

    public MessageLogAfter getMessageAfter() {
        return messageAfter;
    }
}
