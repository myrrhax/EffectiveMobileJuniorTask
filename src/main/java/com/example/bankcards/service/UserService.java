package com.example.bankcards.service;

import com.example.bankcards.dto.UpdateUserDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ApplicationException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserDto getUser(UUID id) {
        return userMapper.toDto(getUserById(id));
    }

    private User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto opUser(UUID userId) {
        log.info("Updating user roles for user {}", userId);
        User user = getUserById(userId);

        if (user.containsRole(Role.ADMIN)) {
            throw new ApplicationException("user.error.already-admin", HttpStatus.BAD_REQUEST, userId);
        }

        user.addRole(Role.ADMIN);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(UUID userId) {
        log.info("Deleting user {}", userId);
        User user = getUserById(userId);
        if (user.containsRole(Role.ADMIN)) {
            throw new ApplicationException("user.error.cannot-delete-admin", HttpStatus.FORBIDDEN, userId);
        }

        userRepository.delete(user);
    }

    public UserDto updateUser(UUID userId, @Valid UpdateUserDto dto) {
        User user = getUserById(userId);
        if (!dto.login().isBlank()) {
            user.setLogin(dto.login());
        }
        if (!dto.email().isBlank()) {
            user.setEmail(dto.email());
        }
        if (!dto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
