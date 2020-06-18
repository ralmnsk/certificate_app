package com.epam.esm.service.exception;

public class NotFoundException extends GeneralException {

    public NotFoundException(Number id) {
        super("Could not find entity id: " + id + ". ");
    }
}
