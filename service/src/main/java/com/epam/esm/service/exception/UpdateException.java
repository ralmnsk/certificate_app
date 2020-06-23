package com.epam.esm.service.exception;

/**
 * The type Update exception.
 * Thrown to indicate that exception happened during the updating process.
 */
public class UpdateException extends GeneralException {
    /**
     * Instantiates a new Update exception.
     * Constructor with the specified detail message.
     *
     * @param id the id
     */
    public UpdateException(Long id) {
        super("Could not find entity id: " + id + ". ");
    }
}
