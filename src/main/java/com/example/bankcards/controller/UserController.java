package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.security.JwtUser;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal JwtUser jwtUser) {
        log.info("Processing get me request for user: {}", jwtUser.getId());
        return ResponseEntity.ok(
                userService.getUser(jwtUser.getId())
        );
    }
}
