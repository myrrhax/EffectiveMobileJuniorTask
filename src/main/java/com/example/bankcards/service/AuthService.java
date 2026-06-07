package com.example.bankcards.service;

import com.example.bankcards.dto.AuthDto;
import com.example.bankcards.dto.LoginUserDto;
import com.example.bankcards.dto.RegisterUserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ApplicationException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtFactory;
import com.example.bankcards.util.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final JwtFactory jwtFactory;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDto register(RegisterUserDto dto) {
        log.info("Trying to register user with login: {} and email: {}", dto.login(), dto.email());
        if (userRepository.existsByLoginOrEmail(dto.login(), dto.email())) {
            throw new ApplicationException("user.duplicated", HttpStatus.CONFLICT, dto.login(), dto.email());
        }

        User user = User.builder()
                .login(dto.login())
                .email(dto.email())
                .passwordHash(passwordEncoder.encode(dto.password()))
                .build();
        user.addRole(Role.USER);
        User createdUser = userRepository.save(user);

        return buildAuthResponse(createdUser);
    }

    public AuthDto login(LoginUserDto dto) {
        log.info("Trying to authenticate user with login: {}", dto.login());

        User user = userRepository.findByLogin(dto.login())
                .orElseThrow(() -> new UserNotFoundException(dto.login()));

        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())) {
            throw new ApplicationException("user.error.invalid-password", HttpStatus.BAD_REQUEST);
        }

        return buildAuthResponse(user);
    }

    private AuthDto buildAuthResponse(User user) {
        return new AuthDto(
                userMapper.toDto(user),
                jwtFactory.generateToken(user)
        );
    }
}
