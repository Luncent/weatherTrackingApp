package org.example.exception_handling.exceptions.service;

import lombok.Getter;
import org.example.enums.AuthErrorType;

@Getter
public class AuthException extends RuntimeException {
    private final AuthErrorType errorType;
    public AuthException(String message, AuthErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
