package com.isc.authentication.utils.exception;


public class SyntaxErrorException extends RuntimeException {

    /**
     * Constructs an instance of <code>SintatticErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SyntaxErrorException(String msg) {
        super(msg);
    }
}
