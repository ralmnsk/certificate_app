package com.epam.esm.service.exception;

/**
 * The type Inconsistency id exception.
 * Thrown to indicate an inconsistency between id and an entity in the database.
 */
public class InconsistencyIdException extends GeneralException {
    /**
     * Instantiates a new Inconsistency id exception.
     * Constructor with the specified detail message.
     *
     * @param message the message
     */
    public InconsistencyIdException(String message) {
        super(message);
    }
}

