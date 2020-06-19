package com.epam.esm.service.dto;

/**
 * The type Exception response.
 */
public class ExceptionResponse {
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
