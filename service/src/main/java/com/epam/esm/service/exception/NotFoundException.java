package com.epam.esm.service.exception;

public class NotFoundException extends GeneralException { //https://spring.io/guides/tutorials/rest/

    public NotFoundException(Long id, Exception e) {
        super("Could not find entity id:" + id + " " + e.getMessage());
    }

    public NotFoundException(Integer id, Exception e) {
        super("Could not find entity id:" + id + " " + e.getMessage());
    }

    public NotFoundException(String message) {
        super(message);
    }
}
