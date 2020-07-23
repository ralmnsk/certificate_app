package com.epam.esm.repository.exception;

import com.epam.esm.repository.exception.GeneralException;

public class SaveException extends GeneralException {


    public SaveException(String message) {
        super("Entity save exception: " + message);
    }
}
