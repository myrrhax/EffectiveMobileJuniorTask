package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @Size(min = 3, max = 15, message = "{validation.login.login-size}")
        @NotBlank(message = "{validation.login.not-blank}")
        String login,

        @Email(message = "{validation.email.invalid-email}")
        @NotBlank(message = "{validation.email.not-blank}")
        String email,

        @NotBlank(message = "{validation.password.not-blank}")
        @Size(min = 8, message = "{validation.password.min}")
        String password
) { }
