package org.example.socialnetwork.security.exception;

import org.springframework.http.HttpStatus;

public class JwtAuthenticationException extends RuntimeException {
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}