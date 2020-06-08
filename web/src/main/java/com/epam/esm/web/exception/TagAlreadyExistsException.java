package com.epam.esm.web.exception;

public class TagAlreadyExistsException extends Exception {
    public TagAlreadyExistsException() {
        super("Tag already exists");
    }
}
