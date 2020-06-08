package com.epam.esm.web.exception;

public class CertificateAlreadyExistsException extends Exception {
    public CertificateAlreadyExistsException() {
        super("Certificate already exists");
    }
}
