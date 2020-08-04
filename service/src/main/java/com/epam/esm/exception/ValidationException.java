package com.epam.esm.exception;

import com.epam.esm.repository.exception.GeneralException;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends GeneralException {
    private Map<String,String> fieldsException = new HashMap<>();

    public ValidationException(String message) {
        super(message);
    }

    public Map<String, String> getFieldsException() {
        return fieldsException;
    }

    public void setFieldsException(Map<String, String> fieldsException) {
        this.fieldsException = fieldsException;
    }
}
