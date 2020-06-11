package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.exception.GeneralException;

public class CertificateUpdateException extends GeneralException {
//    public CertificateUpdateException(CertificateDto certificateDto) {
//        super("Certificate update exception: " + certificateDto);
//    }
//    public CertificateUpdateException(String message) {
//        super("Certificate update exception: " + message);
//    }
    public CertificateUpdateException(Long id, Exception e){
        super("Could not find certificate id:" +id+ " "+e.getMessage());
    }
}
