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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

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
    public ExceptionResponseDto notFoundException(NotFoundException ex) {
        return new ExceptionResponseDto("NotFoundException", ex.getMessage());
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
    public ExceptionResponseDto certificateUpdateException(UpdateException ex) {
        return new ExceptionResponseDto("UpdateException", ex.getMessage());
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
    public ExceptionResponseDto certificateSaveException(SaveException ex) {
        return new ExceptionResponseDto("SaveException", ex.getMessage());
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
    public ExceptionResponseDto incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return new ExceptionResponseDto("IncorrectResultSizeDataAccessException", ex.getMessage());
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
    public ExceptionResponseDto methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Optional<String> message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((a, b) -> a + ", " + b);
        return new ExceptionResponseDto("MethodArgumentNotValidException", message.get());
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
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable e = ex;
        String message = e.getMessage();
        while (e != null) {
            message = e.getMessage();
            e = e.getCause();
        }
        return new ExceptionResponseDto("HttpMessageNotReadableException", message);
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
    public ExceptionResponseDto methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponseDto("MethodArgumentTypeMismatchException",
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
    public ExceptionResponseDto jsonParseException(JsonParseException ex) {
        return new ExceptionResponseDto("JsonParseException", ex.getMessage());
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
    public ExceptionResponseDto noHandlerFoundException(NoHandlerFoundException ex) {
        return new ExceptionResponseDto("NoHandlerFoundException", ex.getMessage());
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
    public ExceptionResponseDto noHandlerException(NoHandlerException ex) {
        return new ExceptionResponseDto("NoHandlerException", ex.getMessage());
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
    public ExceptionResponseDto constraintViolationException(ConstraintViolationException ex) {
        return new ExceptionResponseDto("ConstraintViolationException",
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
    public ExceptionResponseDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ExceptionResponseDto("HttpRequestMethodNotSupportedException",
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
    public ExceptionResponseDto inconsistencyIdException(InconsistencyIdException ex) {
        return new ExceptionResponseDto("InconsistencyIdException",
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
    public ExceptionResponseDto idInNewTagException(NewTagHasIdInCertificateException ex) {
        return new ExceptionResponseDto("IdInNewTagException",
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
    public ExceptionResponseDto dataAccessResourceFailureException(DataAccessResourceFailureException ex) {
        return new ExceptionResponseDto("DataAccessResourceFailureException",
                "Failed to obtain JDBC Connection.");
    }

    /**
     * Throwable handler returns ExceptionResponse in JSON
     * and ResponseStatus BAD_REQUEST
     *
     * @param ex the Throwable
     * @return the exception response
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponseDto throwable(Throwable ex) {
        Throwable e = ex;
        String message = e.getMessage();
        while (e != null) {
            message = e.getMessage();
            e = e.getCause();
        }
        return new ExceptionResponseDto("Exception",
                "Exception happened:" + message);
    }
}
