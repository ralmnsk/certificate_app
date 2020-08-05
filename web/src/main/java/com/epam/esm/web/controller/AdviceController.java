package com.epam.esm.web.controller;

import com.epam.esm.dto.ExceptionResponseDto;
import com.epam.esm.dto.ExceptionResponseDtoList;
import com.epam.esm.exception.*;
import com.epam.esm.repository.exception.*;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
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
    public ExceptionResponseDtoList methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ExceptionResponseDtoList list = new ExceptionResponseDtoList("MethodArgumentNotValidException", "Argument(s) of entity not valid.");

        ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> errors.put(e.getField().equals("login") ? "email" : e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());

        list.setFields(errors);
        return list;
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ExceptionResponseDto httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        if (ex.getCause() != null) {
            message = ex.getCause().getMessage();
        }
        return new ExceptionResponseDto("HttpMessageNotReadableException", message);
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = BAD_REQUEST)
    public ExceptionResponseDtoList validationException(ValidationException ex) {
        Map<String, String> errors = ex.getFieldsException();

        if (errors.isEmpty()) {
            ExceptionResponseDtoList exception = new ExceptionResponseDtoList("ValidationException");
            return exception;
        }
        ExceptionResponseDtoList list = new ExceptionResponseDtoList("ValidationException", "Validation exception happened.");
        list.setFields(errors);
        return list;
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
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
    @ExceptionHandler(JsonParseCustomException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto exception(JsonParseCustomException ex) {
        return new ExceptionResponseDto("JsonParseCustomException", ex.getMessage());
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
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponseDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ExceptionResponseDto("HttpRequestMethodNotSupportedException",
                ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(JwtUserAuthenticationException.class)
    @ResponseStatus(FORBIDDEN)
    public ExceptionResponseDto exception(JwtUserAuthenticationException ex) {
        return new ExceptionResponseDto("JwtUserAuthenticationException",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AccessException.class)
    @ResponseStatus(FORBIDDEN)
    public ExceptionResponseDto exception(AccessException ex) {
        return new ExceptionResponseDto("AccessException",
                ex.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponseDto exception(UsernameNotFoundException ex) {
        return new ExceptionResponseDto("UsernameNotFoundException",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(JsonSerializationException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponseDto exception(JsonSerializationException ex) {
        return new ExceptionResponseDto("JsonSerializationException",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DeleteException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ExceptionResponseDto exception(DeleteException ex) {
        return new ExceptionResponseDto("DeleteException", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponseDtoList exception(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        if (constraintViolations.isEmpty()) {
            ExceptionResponseDtoList exception = new ExceptionResponseDtoList("ConstraintViolationException");
            return exception;
        }
        ExceptionResponseDtoList list = new ExceptionResponseDtoList("ConstraintViolationException", "Constraint Violation exception happened.");
        Map<String, String> errors = new HashMap<>();
        constraintViolations.forEach(c->errors.put(c.getPropertyPath().toString(),c.getMessage()));
        list.setFields(errors);
        return list;
//        ex.getConstraintViolations().
//        return new ExceptionResponseDto("ConstraintViolationException", ex.getMessage());
    }


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
        log.error(message);
        return new ExceptionResponseDto("Exception",
                "Exception happened:" + message);
    }
}
