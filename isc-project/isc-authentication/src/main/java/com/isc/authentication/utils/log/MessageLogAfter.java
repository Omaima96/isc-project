package com.isc.authentication.utils.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageLogAfter {

    private JoinPoint joinPoint;

    private Object response;

    private String user;

}
