package com.epam.esm.service.exception;

import com.epam.esm.service.dto.CertificateDto;

public class CertificateUpdateException extends RuntimeException {
    public CertificateUpdateException(CertificateDto certificateDto) {
        super("Certificate update exception: " + certificateDto);
    }
    public CertificateUpdateException(String message) {
        super("Certificate update exception: " + message);
    }
}
