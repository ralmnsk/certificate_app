package com.epam.esm.web.controller;

import com.epam.esm.web.exception.TagAlreadyExistsException;
import com.epam.esm.web.exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TagAdvice {

    @ResponseBody
    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tagNotFoundException(TagNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TagAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String tagAlreadyExistsException(TagAlreadyExistsException ex) {
        return ex.getMessage();
    }

}
