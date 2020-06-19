package com.epam.esm.service.exception;

/**
 * The type Update exception.
 */
public class UpdateException extends GeneralException {
    /**
     * Instantiates a new Update exception.
     *
     * @param id the id
     */
    public UpdateException(Long id) {
        super("Could not find entity id: " + id + ". ");
    }
}
