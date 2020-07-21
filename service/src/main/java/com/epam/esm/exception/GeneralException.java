package com.epam.esm.exception;

/**
 * The type General exception.
 * The GeneralException and its subclasses are unchecked
 * exceptions that do not need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 */
public class GeneralException extends RuntimeException {
    /**
     * Instantiates a new General exception.
     * Constructor with the specified detail message.
     *
     * @param message the message
     */
    public GeneralException(String message) {
        super(message);
    }

}
