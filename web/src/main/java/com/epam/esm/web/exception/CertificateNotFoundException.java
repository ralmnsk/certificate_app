package com.epam.esm.web.exception;

public class CertificateNotFoundException extends RuntimeException { //https://spring.io/guides/tutorials/rest/
    public CertificateNotFoundException(Long id){
        super("Could not find certificate " +id);
    }
}
