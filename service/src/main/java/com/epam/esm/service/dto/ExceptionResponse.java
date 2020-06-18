package com.epam.esm.service.dto;

public class ExceptionResponse {
    private String exception;
    private String message;

    public ExceptionResponse(String exception, String message) {
        this.exception = exception;
        this.message = message;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionResponse that = (ExceptionResponse) o;

        if (!exception.equals(that.exception)) return false;
        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        int result = exception.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "exception='" + exception + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
