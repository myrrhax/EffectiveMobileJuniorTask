package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String login,
        String email,
        LocalDateTime createdAt,
        Set<Role> roles
) { }
