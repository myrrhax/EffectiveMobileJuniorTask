package com.example.bankcards.util;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DefaultAdminRunner implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${app.admin.name}")
    private String defaultAdminLogin;
    @Value("${app.admin.password}")
    private String defaultAdminPass;
    @Value("${app.admin.email}")
    private String defaultAdminEmail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database with default admin data");

        if (!userRepository.hasUsersWithRole(Role.ADMIN)) {
            log.info("Database has no data");

            User user = User.builder()
                    .login(defaultAdminLogin)
                    .email(defaultAdminEmail)
                    .passwordHash(passwordEncoder.encode(defaultAdminPass))
                    .roles(Set.of(Role.ADMIN))
                    .build();
            userRepository.save(user);

            log.info("Default admin was added");
        } else {
            log.info("Database was already populated");
        }
    }
}
