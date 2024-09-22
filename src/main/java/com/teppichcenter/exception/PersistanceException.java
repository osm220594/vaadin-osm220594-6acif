package com.teppichcenter.exception;

/**
 * @author SLM
 */
public class PersistanceException extends RuntimeException {
    public PersistanceException(String message) {
        super(message);
    }
    public PersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
