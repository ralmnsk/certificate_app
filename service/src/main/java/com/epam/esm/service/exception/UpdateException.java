package com.epam.esm.service.exception;

public class UpdateException extends GeneralException {
    public UpdateException(Long id, Exception e) {
        super("Could not find entity id: " + id + " " + e.getMessage());
    }
}
