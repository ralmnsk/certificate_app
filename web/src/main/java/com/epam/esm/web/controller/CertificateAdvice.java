package com.epam.esm.web.controller;

import com.epam.esm.service.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.exception.certificate.CertificateSaveException;
import com.epam.esm.service.exception.certificate.CertificateUpdateException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CertificateAdvice {

    //https://tools.ietf.org/html/rfc7231 This is status codes
    @ResponseBody // to return text of exception
    @ExceptionHandler(CertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String certificateNotFoundException(CertificateNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CertificateUpdateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   //409
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
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String incorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex){
        return ex.getMessage();
    }

}
