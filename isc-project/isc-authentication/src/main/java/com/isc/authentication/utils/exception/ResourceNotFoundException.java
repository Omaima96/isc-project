package com.isc.authentication.utils.exception;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>ResourceNotFoundException</code> without
     * detail message.
     */
    public ResourceNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ResourceNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
