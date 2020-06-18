package com.epam.esm.web.controller;

import com.epam.esm.service.dto.ExceptionResponse;
import com.epam.esm.service.exception.NoHandlerException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class AdviceController {


    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse certificateNotFoundException(NotFoundException ex) {
        return new ExceptionResponse("NotFoundException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse certificateUpdateException(UpdateException ex) {
        return new ExceptionResponse("UpdateException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(SaveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse certificateSaveException(SaveException ex) {
        return new ExceptionResponse("SaveException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return new ExceptionResponse("IncorrectResultSizeDataAccessException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ExceptionResponse("MethodArgumentNotValidException",
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ExceptionResponse("HttpMessageNotReadableException",
                Objects.requireNonNull(ex.getRootCause()).getMessage());
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponse("MethodArgumentTypeMismatchException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse jsonParseException(JsonParseException ex) {
        return new ExceptionResponse("JsonParseException", ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponse("NoHandlerFoundException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerException(NoHandlerException ex) {
        return new ExceptionResponse("NoHandlerException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse constraintViolationException(ConstraintViolationException ex) {
        return new ExceptionResponse("ConstraintViolationException",
                ex.getMessage());
    }
}
