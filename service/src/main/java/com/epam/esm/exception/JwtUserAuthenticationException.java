package com.epam.esm.exception;


public class JwtUserAuthenticationException extends GeneralException {
    public JwtUserAuthenticationException(String explanation) {
        super(explanation);
    }

}
