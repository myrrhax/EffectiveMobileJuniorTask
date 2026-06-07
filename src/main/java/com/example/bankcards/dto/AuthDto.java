package com.example.bankcards.dto;

public record AuthDto(
    UserDto user,
    String accessToken
) { }
