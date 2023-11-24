package com.isc.authentication.utils.exception;

import org.springframework.security.core.AuthenticationException;


public class UnauthorizedException extends AuthenticationException {

    /**
     * Constructs an instance of <code>UnauthorizedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
