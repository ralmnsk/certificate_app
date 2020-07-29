package com.epam.esm.repository.exception;

public class NotFoundException extends GeneralException {

    public NotFoundException(Number id) {
        super("Could not find entity id: " + id + ". ");
    }

    public NotFoundException(String str) {
        super(str);
    }
}
