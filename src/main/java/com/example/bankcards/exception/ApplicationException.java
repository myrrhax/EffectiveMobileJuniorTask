package com.example.bankcards.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private String messageSource;
    private HttpStatus status;
    private Object[] args;

    public ApplicationException(String message, HttpStatus status, Object... args) {
        super(message);
    }
}
