package com.epam.esm.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * The type Exception response.
 */
public class ExceptionResponse implements Serializable {
    private String exception;
    private String message;

    /**
     * Instantiates a new Exception response.
     *
     * @param exception the exception
     * @param message   the message
     */
    public ExceptionResponse(String exception, String message) {
        this.exception = exception;
        this.message = message;
    }

    /**
     * Gets exception.
     *
     * @return the exception
     */
    public String getException() {
        return exception;
    }

    /**
     * Sets exception.
     *
     * @param exception the exception
     */
    public void setException(String exception) {
        this.exception = exception;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ExceptionResponse o = (ExceptionResponse) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.exception, o.getException())
                .append(this.message, o.getMessage())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(exception)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
