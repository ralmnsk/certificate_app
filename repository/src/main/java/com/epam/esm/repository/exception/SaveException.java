package com.epam.esm.repository.exception;

public class SaveException extends GeneralException {


    public SaveException(String message) {
        super("Entity save exception: " + message);
    }
}
