package com.isc.authentication.utils.exception;


public class SemanticErrorException extends RuntimeException {

    /**
     * Constructs an instance of <code>SintatticErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SemanticErrorException(String msg) {
        super(msg);
    }
}
