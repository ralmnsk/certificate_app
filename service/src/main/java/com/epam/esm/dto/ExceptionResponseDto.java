package com.epam.esm.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class ExceptionResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String exception;
    private String message;

    public ExceptionResponseDto(String exception, String message) {
        this.exception = exception;
        this.message = message;
    }

    public ExceptionResponseDto(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

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
        ExceptionResponseDto o = (ExceptionResponseDto) obj;
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
