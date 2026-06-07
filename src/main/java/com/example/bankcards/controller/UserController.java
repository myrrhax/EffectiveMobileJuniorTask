package com.example.bankcards.controller;

import com.example.bankcards.dto.UpdateUserDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.security.JwtUser;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getUsers(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        log.info("Fetching users request for page: {}", pageable.getPageNumber());
        List<UserDto> users = userService.getUsers(pageable);

        return users.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(users);
    }

    @PostMapping("{userId}/op")
    public ResponseEntity<UserDto> opUser(@PathVariable UUID userId) {
        log.info("Processing op request for user: {}", userId);

        return ResponseEntity.ok(userService.opUser(userId));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("Processing delete request for user: {}", userId);
        userService.deleteUser(userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
                                              @Valid @RequestBody UpdateUserDto dto) {
        log.info("Processing update request for user: {}", userId);

        return ResponseEntity.ok(
                userService.updateUser(userId, dto)
        );
    }
}
