package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String login) {
        super("user.error.not-found", HttpStatus.NOT_FOUND, login);
    }
}
