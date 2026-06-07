package com.example.bankcards.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDto(
        @Size(min = 3, max = 15, message = "{validation.login.login-size}")
        @NotBlank(message = "{validation.login.not-blank}")
        String login,

        @NotBlank(message = "{validation.password.not-blank}")
        @Size(min = 8, message = "{validation.password.min}")
        String password
) { }