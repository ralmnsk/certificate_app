package com.epam.esm.service.exception;

import com.epam.esm.service.dto.CertificateDto;

/**
 * The type Save exception.
 */
public class SaveException extends GeneralException {
    /**
     * Instantiates a new Save exception.
     *
     * @param certificateDto the certificate dto
     */
    public SaveException(CertificateDto certificateDto) {
        super("Entity save exception: " + certificateDto);
    }

    /**
     * Instantiates a new Save exception.
     *
     * @param message the message
     */
    public SaveException(String message) {
        super("Entity save exception: " + message);
    }
}
