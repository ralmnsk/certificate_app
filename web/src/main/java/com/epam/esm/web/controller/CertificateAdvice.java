package com.epam.esm.web.controller;

import com.epam.esm.web.exception.CertificateAlreadyExistsException;
import com.epam.esm.web.exception.CertificateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CertificateAdvice {

    @ResponseBody
    @ExceptionHandler(CertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String certificateNotFoundException(CertificateNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CertificateAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String certificateAlreadyExistsException(CertificateAlreadyExistsException ex) {
        return ex.getMessage();
    }

}
