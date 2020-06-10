package com.epam.esm.service.exception;

import com.epam.esm.service.dto.CertificateDto;

public class CertificateSaveException extends RuntimeException {
    public CertificateSaveException(CertificateDto certificateDto) {
        super("Certificate save exception: " + certificateDto);
    }
    public CertificateSaveException(String message) {
        super("Certificate save exception: " + message);
    }
}
