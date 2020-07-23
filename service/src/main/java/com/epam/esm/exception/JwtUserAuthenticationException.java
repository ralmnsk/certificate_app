package com.epam.esm.exception;


import com.epam.esm.repository.exception.GeneralException;

public class JwtUserAuthenticationException extends GeneralException {
    public JwtUserAuthenticationException(String explanation) {
        super(explanation);
    }

}
