package com.epam.esm.exception;

/**
 * The type No handler exception.
 * Thrown to indicate that an application hasn't such handler.
 */
public class NoHandlerException extends GeneralException {

    /**
     * Instantiates a new No handler exception.
     * Constructor with the specified detail message.
     *
     * @param message the message
     */
    public NoHandlerException(String message) {
        super(message);
    }
}
