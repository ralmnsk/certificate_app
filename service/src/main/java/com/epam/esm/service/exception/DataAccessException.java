package com.epam.esm.service.exception;

/**
 * The type Data access exception.
 * Thrown to indicate that application has attempted to access a database, but
 * has no access to data
 */
public class DataAccessException extends GeneralException {
    /**
     * Instantiates a new Data access exception.
     * Constructor with the specified detail message.
     *
     * @param message the message to describe input causing the error
     */
    public DataAccessException(String message) {
        super(message);
    }
}
