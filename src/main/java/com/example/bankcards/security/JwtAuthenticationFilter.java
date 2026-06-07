package com.example.bankcards.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final JpaUserDetailsService userDetailsService;
    private final JwtFactory jwtFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
          String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

          if (authHeader != null && authHeader.startsWith(BEARER)) {
              try {
                  String token = authHeader.substring(BEARER.length());
                  if (!token.isEmpty()) {
                        String login = jwtFactory.getLogin(token);

                        if (login != null && !login.isEmpty()) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(login);

                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails,
                                            null,
                                            userDetails.getAuthorities());

                            SecurityContext context = SecurityContextHolder.createEmptyContext();
                            context.setAuthentication(authentication);
                            SecurityContextHolder.setContext(context);
                        }
                  }
              } catch (Exception e) {
                  log.error("Failed to authenticate user", e);
              }
          }

          filterChain.doFilter(request, response);
    }
}
