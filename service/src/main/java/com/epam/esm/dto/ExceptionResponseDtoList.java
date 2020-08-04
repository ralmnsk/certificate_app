package com.epam.esm.dto;

import java.util.Map;

public class ExceptionResponseDtoList extends ExceptionResponseDto {
    private Map<String,String> fields;
    public ExceptionResponseDtoList(String exception, String message) {
        super(exception, message);
    }
    public ExceptionResponseDtoList(String exception){
        super(exception);
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
