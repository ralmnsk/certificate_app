package com.epam.esm.service.exception;

public class CertificateAlreadyExistsException extends RuntimeException {
    public CertificateAlreadyExistsException() {
        super("Certificate already exists");
    }
}
