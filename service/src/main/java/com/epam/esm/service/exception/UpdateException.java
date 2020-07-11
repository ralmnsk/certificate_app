package com.epam.esm.service.exception;

public class UpdateException extends GeneralException {
    public UpdateException(Long id) {
        super("Update exception entity id: " + id + ". ");
    }

    public UpdateException(String str) {
        super(str);
    }
}
