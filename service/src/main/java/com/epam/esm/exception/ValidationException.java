package com.epam.esm.exception;

import com.epam.esm.repository.exception.GeneralException;

public class ValidationException extends GeneralException {
    public ValidationException(String message) {
        super(message);
    }
}
