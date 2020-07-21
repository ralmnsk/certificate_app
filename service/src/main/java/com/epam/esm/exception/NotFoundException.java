package com.epam.esm.exception;

public class NotFoundException extends GeneralException {

    public NotFoundException(Number id) {
        super("Could not find entity id: " + id + ". ");
    }

    public NotFoundException(String str) {
        super("Could not find entity: " + str + ". ");
    }
}
