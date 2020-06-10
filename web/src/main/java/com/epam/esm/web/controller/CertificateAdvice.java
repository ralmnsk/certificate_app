package com.epam.esm.web.controller;

import com.epam.esm.service.exception.CertificateAlreadyExistsException;
import com.epam.esm.service.exception.CertificateNotFoundException;
import com.epam.esm.service.exception.CertificateSaveException;
import com.epam.esm.service.exception.CertificateUpdateException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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

    @ResponseBody
    @ExceptionHandler(CertificateUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String certificateUpdateException(CertificateUpdateException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CertificateSaveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String certificateSaveException(CertificateSaveException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex){
        return ex.getMessage();
    }
}
