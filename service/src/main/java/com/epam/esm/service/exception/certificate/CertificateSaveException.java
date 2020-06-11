package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.exception.GeneralException;

public class CertificateSaveException extends GeneralException {
    public CertificateSaveException(CertificateDto certificateDto) {
        super("Certificate save exception: " + certificateDto);
    }
    public CertificateSaveException(String message) {
        super("Certificate save exception: " + message);
    }
}
