package com.isc.authentication.utils.exception;

import org.springframework.security.core.AuthenticationException;


public class UnauthenticatedException extends AuthenticationException {

    /**
     * Constructs an instance of <code>UnauthorizedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnauthenticatedException(String msg) {
        super(msg);
    }
}
