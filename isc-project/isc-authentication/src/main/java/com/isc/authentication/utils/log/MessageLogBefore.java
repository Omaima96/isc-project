package com.isc.authentication.utils.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageLogBefore {

    private JoinPoint joinPoint;

    private String user;
}
