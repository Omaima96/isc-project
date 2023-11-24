package com.isc.authentication.utils.messaging;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailMessage implements Serializable {

    private String from;

    private String replyTo;

    private String to;

    private String cc;

    private String bcc;

    private Date sentDate;

    private String subject;

    private String text;

    private String template;

    private Map<String, String> params = new HashMap<>();

}
