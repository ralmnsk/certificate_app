package com.epam.esm.service.exception;

public class UpdateException extends GeneralException {
    public UpdateException(Long id) {
        super("Could not find entity id: " + id + ". ");
    }
}
