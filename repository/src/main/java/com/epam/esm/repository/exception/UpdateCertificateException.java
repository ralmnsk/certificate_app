package com.epam.esm.repository.exception;

public class UpdateCertificateException extends Exception {
    public UpdateCertificateException(String message){
        super("Update certificate exception: " + message);
    }
}
