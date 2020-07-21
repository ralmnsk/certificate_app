package com.epam.esm.exception;

/**
 * The type Id in new tag exception.
 * Thrown to indicate that new Tag Entity in the new Certificate during
 * post request can not have an id.
 */
public class NewTagHasIdInCertificateException extends GeneralException {
    /**
     * Instantiates a new Id in new tag exception.
     * Constructor with the specified detail message.
     *
     * @param message the message
     */
    public NewTagHasIdInCertificateException(String message) {
        super(message);
    }
}
