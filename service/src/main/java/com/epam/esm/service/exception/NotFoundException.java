package com.epam.esm.service.exception;

/**
 * The type Not found exception.
 */
public class NotFoundException extends GeneralException {

    /**
     * Instantiates a new Not found exception.
     *
     * @param id the id
     */
    public NotFoundException(Number id) {
        super("Could not find entity id: " + id + ". ");
    }
}
