package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(min = 3, max = 15, message = "{validation.login.login-size}")
        String login,

        @Email(message = "{validation.email.invalid-email}")
        String email,

        @Size(min = 8, message = "{validation.password.min}")
        String password
) { }
