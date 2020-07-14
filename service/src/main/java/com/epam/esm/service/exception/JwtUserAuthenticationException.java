package com.epam.esm.service.exception;


import com.epam.esm.service.exception.GeneralException;

public class JwtUserAuthenticationException extends GeneralException {
    public JwtUserAuthenticationException(String explanation) {
        super(explanation);
    }

}
