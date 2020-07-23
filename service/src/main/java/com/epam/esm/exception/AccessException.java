package com.epam.esm.exception;

import com.epam.esm.repository.exception.GeneralException;

public class AccessException extends GeneralException {
    public AccessException(String message) {
        super(message);
    }
}
