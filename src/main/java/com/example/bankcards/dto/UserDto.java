package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String login,
        String email,
        Instant createdAt,
        Set<Role> roles
) { }
