package com.example.bankcards.security;

import com.example.bankcards.config.properties.JwtProperties;
import com.example.bankcards.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFactory {
    private static final String ROLES_CLAIM = "roles";
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public String generateToken(User user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getLogin())
                .issuer(jwtProperties.issuer())
                .claim(ROLES_CLAIM, user.getRoles())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.lifetime())))
                .signWith(secretKey)
                .compact();
    }

    public String getLogin(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
