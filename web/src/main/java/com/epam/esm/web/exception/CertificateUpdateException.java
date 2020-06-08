package com.epam.esm.web.exception;

import com.epam.esm.service.dto.CertificateDto;

public class CertificateUpdateException extends Exception {
    public CertificateUpdateException(CertificateDto certificateDto) {
        super("Certificate update exception: " + certificateDto);
    }
}
