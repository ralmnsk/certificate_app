package com.epam.esm.web.controller;

import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class AdviceController {

    //https://tools.ietf.org/html/rfc7231 This is status codes
    @ResponseBody // to return text of exception
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String certificateNotFoundException(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   //409
    public String certificateUpdateException(UpdateException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SaveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String certificateSaveException(SaveException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ex.getMessage();
    }
}
