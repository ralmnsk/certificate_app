package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.exception.GeneralException;

public class CertificateNotFoundException extends GeneralException { //https://spring.io/guides/tutorials/rest/
    public CertificateNotFoundException(Long id, Exception e){
        super("Could not find certificate id:" +id+ " "+e.getMessage());
    }
    public CertificateNotFoundException(String message){
        super(message);
    }
}
