package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String login) {
        super("user.error.not-found", HttpStatus.NOT_FOUND, login);
    }

    public UserNotFoundException(UUID id) {
        super("user.error.not-found-by-id", HttpStatus.NOT_FOUND, id);
    }
}
