package com.epam.esm.service.exception;

/**
 * The type Not found exception.
 * Thrown to indicate that resource hasn't been found.
 */
public class NotFoundException extends GeneralException {

    /**
     * Instantiates a new Not found exception.
     * Constructor with the specified detail message.
     *
     * @param id the id
     */
    public NotFoundException(Number id) {
        super("Could not find entity id: " + id + ". ");
    }
}
