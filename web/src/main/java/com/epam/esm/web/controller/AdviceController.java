package com.epam.esm.web.controller;

import com.epam.esm.service.dto.ExceptionResponse;
import com.epam.esm.service.exception.*;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * The type Advice controller.
 */
@ControllerAdvice
public class AdviceController {


    /**
     * Certificate not found exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse certificateNotFoundException(NotFoundException ex) {
        return new ExceptionResponse("NotFoundException", ex.getMessage());
    }

    /**
     * Certificate update exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse certificateUpdateException(UpdateException ex) {
        return new ExceptionResponse("UpdateException", ex.getMessage());
    }

    /**
     * Certificate save exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(SaveException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse certificateSaveException(SaveException ex) {
        return new ExceptionResponse("SaveException", ex.getMessage());
    }

    /**
     * Incorrect result size data access exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return new ExceptionResponse("IncorrectResultSizeDataAccessException", ex.getMessage());
    }

    /**
     * Method argument not valid exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder(ex.getMessage());
        if (ex.getBindingResult().getAllErrors().get(0).getDefaultMessage() != null) {
            message = new StringBuilder(Objects.requireNonNull(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
            String[] codes = ex.getBindingResult().getAllErrors().get(0).getCodes();
            if (codes != null && codes.length >= 1) {
                message = new StringBuilder(codes[0]).append(" ").append(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            }
        }
        return new ExceptionResponse("MethodArgumentNotValidException",
                message.toString());
    }

    /**
     * Http message not readable exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ExceptionResponse("HttpMessageNotReadableException",
                Objects.requireNonNull(ex.getCause()).getMessage());
    }

    /**
     * Method argument type mismatch exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponse("MethodArgumentTypeMismatchException",
                ex.getName() + " argument mismatch " + ex.getCause().getMessage().toLowerCase());
    }

    /**
     * Json parse exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse jsonParseException(JsonParseException ex) {
        return new ExceptionResponse("JsonParseException", ex.getMessage());
    }


    /**
     * No handler found exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponse("NoHandlerFoundException", ex.getMessage());
    }

    /**
     * No handler exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerException(NoHandlerException ex) {
        return new ExceptionResponse("NoHandlerException", ex.getMessage());
    }

    /**
     * Constraint violation exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse constraintViolationException(ConstraintViolationException ex) {
        return new ExceptionResponse("ConstraintViolationException",
                ex.getMessage());
    }


    /**
     * Http request method not supported exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ExceptionResponse("HttpRequestMethodNotSupportedException",
                ex.getMessage());
    }


    /**
     * Inconsistency id exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(InconsistencyIdException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse inconsistencyIdException(InconsistencyIdException ex) {
        return new ExceptionResponse("InconsistencyIdException",
                ex.getMessage());
    }


    /**
     * Id in new tag exception exception response.
     *
     * @param ex the ex
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(IdInNewTagException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse idInNewTagException(IdInNewTagException ex) {
        return new ExceptionResponse("IdInNewTagException",
                ex.getMessage());
    }
}
