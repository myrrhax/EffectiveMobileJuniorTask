package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.LoginUserDto;
import com.example.bankcards.dto.RegisterUserDto;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthDto> register(
            @Valid @RequestBody RegisterUserDto dto
    ) {
        AuthDto response = authService.register(dto);
        log.info("User with login {} was registered", response.user().login());

        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<AuthDto> login(
            @Valid @RequestBody LoginUserDto dto
    ) {
        AuthDto response = authService.login(dto);
        log.info("User with login {} was authenticated", response.user().login());

        return ResponseEntity.ok(response);
    }
}
