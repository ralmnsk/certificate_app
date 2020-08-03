package com.epam.esm.exception;

import com.epam.esm.repository.exception.GeneralException;

public class JsonSerializationException extends GeneralException {
    public JsonSerializationException(String message) {
        super(message);
    }
}
