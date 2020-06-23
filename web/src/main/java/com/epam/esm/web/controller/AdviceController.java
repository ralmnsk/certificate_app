package com.epam.esm.web.controller;

import com.epam.esm.service.dto.ExceptionResponse;
import com.epam.esm.service.exception.*;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.dao.DataAccessResourceFailureException;
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
 * The AdviceController handles errors that could appear during application work.
 */
@ControllerAdvice
public class AdviceController {


    /**
     * NotFoundException handler returns ExceptionResponse in JSON and ResponseStatus NOT_FOUND
     *
     * @param ex the NotFoundException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException ex) {
        return new ExceptionResponse("NotFoundException", ex.getMessage());
    }

    /**
     * UpdateException handler returns ExceptionResponse in JSON and ResponseStatus NOT_FOUND
     *
     * @param ex the UpdateException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse certificateUpdateException(UpdateException ex) {
        return new ExceptionResponse("UpdateException", ex.getMessage());
    }

    /**
     * SaveException handler returns ExceptionResponse in JSON and ResponseStatus CONFLICT
     *
     * @param ex the SaveException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(SaveException.class)
    @ResponseStatus(CONFLICT)
    public ExceptionResponse certificateSaveException(SaveException ex) {
        return new ExceptionResponse("SaveException", ex.getMessage());
    }

    /**
     * IncorrectResultSizeDataAccessException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the IncorrectResultSizeDataAccessException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return new ExceptionResponse("IncorrectResultSizeDataAccessException", ex.getMessage());
    }

    /**
     * MethodArgumentNotValidException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the MethodArgumentNotValidException
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
     * HttpMessageNotReadableException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the HttpMessageNotReadableException
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
     * MethodArgumentTypeMismatchException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the MethodArgumentTypeMismatchException
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
     * JsonParseException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the JsonParseException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse jsonParseException(JsonParseException ex) {
        return new ExceptionResponse("JsonParseException", ex.getMessage());
    }


    /**
     * NoHandlerFoundException handler returns ExceptionResponse in JSON
     * and ResponseStatus NOT_FOUND
     *
     * @param ex the NoHandlerFoundException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponse("NoHandlerFoundException", ex.getMessage());
    }

    /**
     * NoHandlerException handler returns ExceptionResponse in JSON
     * and ResponseStatus NOT_FOUND
     *
     * @param ex the NoHandlerException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse noHandlerException(NoHandlerException ex) {
        return new ExceptionResponse("NoHandlerException", ex.getMessage());
    }

    /**
     * ConstraintViolationException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the ConstraintViolationException
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
     * HttpRequestMethodNotSupportedException handler returns ExceptionResponse in JSON
     * and ResponseStatus METHOD_NOT_ALLOWED
     *
     * @param ex the HttpRequestMethodNotSupportedException
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
     * InconsistencyIdException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the InconsistencyIdException
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
     * NewTagHasIdInCertificateException handler returns ExceptionResponse in JSON
     * and ResponseStatus UNPROCESSABLE_ENTITY
     *
     * @param ex the NewTagHasIdInCertificateException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(NewTagHasIdInCertificateException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponse idInNewTagException(NewTagHasIdInCertificateException ex) {
        return new ExceptionResponse("IdInNewTagException",
                ex.getMessage());
    }

    /**
     * DataAccessResourceFailureException handler returns ExceptionResponse in JSON
     * and ResponseStatus NOT_ACCEPTABLE
     *
     * @param ex the DataAccessResourceFailureException
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(DataAccessResourceFailureException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ExceptionResponse dataAccessResourceFailureException(DataAccessResourceFailureException ex) {
        return new ExceptionResponse("DataAccessResourceFailureException",
                "Failed to obtain JDBC Connection.");
    }
}
