package com.epam.esm.web.security.jwt;


import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }

    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
