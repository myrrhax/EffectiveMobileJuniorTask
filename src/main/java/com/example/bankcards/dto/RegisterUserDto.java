package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserDto(
        @Min(value = 3, message = "{validation.login.min-login}")
        @Max(value = 15, message = "{validation.login.max-login}")
        @NotBlank(message = "{validation.login.not-blank}")
        String login,

        @Email(message = "{validation.email.invalid-email}")
        @NotBlank(message = "{validation.email.not-blank}")
        String email,

        @NotBlank(message = "{validation.password.not-blank}")
        @Min(value = 8, message = "{validation.password.min}")
        String password
) { }
