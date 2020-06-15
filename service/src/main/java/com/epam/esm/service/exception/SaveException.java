package com.epam.esm.service.exception;

import com.epam.esm.service.dto.CertificateDto;

public class SaveException extends GeneralException {
    public SaveException(CertificateDto certificateDto) {
        super("Entity save exception: " + certificateDto);
    }

    public SaveException(String message) {
        super("Entity save exception: " + message);
    }
}
