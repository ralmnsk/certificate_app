package com.epam.esm.web.controller;

import com.epam.esm.service.dto.ExceptionResponseDto;
import com.epam.esm.service.exception.*;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class AdviceController {


    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponseDto notFoundException(NotFoundException ex) {
        return new ExceptionResponseDto("NotFoundException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponseDto certificateUpdateException(UpdateException ex) {
        return new ExceptionResponseDto("UpdateException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(SaveException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponseDto certificateSaveException(SaveException ex) {
        return new ExceptionResponseDto("SaveException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponseDto incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return new ExceptionResponseDto("IncorrectResultSizeDataAccessException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Optional<String> message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((a, b) -> a + ", " + b);
        return new ExceptionResponseDto("MethodArgumentNotValidException", message.get());
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ExceptionResponseDto("HttpMessageNotReadableException", ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponseDto("MethodArgumentTypeMismatchException",
                ex.getName() + " argument mismatch " + ex.getCause().getMessage().toLowerCase());
    }

    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto jsonParseException(JsonParseException ex) {
        return new ExceptionResponseDto("JsonParseException", ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponseDto noHandlerFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponseDto("NoHandlerFoundException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponseDto noHandlerException(NoHandlerException ex) {
        return new ExceptionResponseDto("NoHandlerException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto constraintViolationException(ConstraintViolationException ex) {
        return new ExceptionResponseDto("ConstraintViolationException",
                ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponseDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ExceptionResponseDto("HttpRequestMethodNotSupportedException",
                ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(InconsistencyIdException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto inconsistencyIdException(InconsistencyIdException ex) {
        return new ExceptionResponseDto("InconsistencyIdException",
                ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(NewTagHasIdInCertificateException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto idInNewTagException(NewTagHasIdInCertificateException ex) {
        return new ExceptionResponseDto("IdInNewTagException",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DataAccessResourceFailureException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ExceptionResponseDto dataAccessResourceFailureException(DataAccessResourceFailureException ex) {
        return new ExceptionResponseDto("DataAccessResourceFailureException",
                "Failed to obtain JDBC Connection.");
    }

    @ExceptionHandler(JsonDeserializationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto exception(JsonDeserializationException ex) {
        return new ExceptionResponseDto("JsonDeserializationException",
                ex.getMessage());
    }


//    @ResponseBody
//    @ExceptionHandler(Throwable.class)
//    @ResponseStatus(BAD_REQUEST)
//    public ExceptionResponseDto throwable(Throwable ex) {
//        Throwable e = ex;
//        String message = e.getMessage();
//        while (e != null) {
//            message = e.getMessage();
//            e = e.getCause();
//        }
//        return new ExceptionResponseDto("Exception",
//                "Exception happened:" + message);
//    }
}
