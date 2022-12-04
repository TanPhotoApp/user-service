package com.photoapp.user.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenProperties tokenProperties;

    public AuthorizationFilter(AuthenticationManager authenticationManager, TokenProperties tokenProperties) {
        super(authenticationManager);
        this.tokenProperties = tokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(authHeader -> authHeader.replace("Bearer ", ""))
            .orElse(null);

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        var authentication = this.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            var userId = Jwts.parserBuilder()
                .setSigningKey(tokenProperties.getSigningKey().getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

            if (userId == null) {
                log.warn("Token contain null userId: {}", token);
                return null;
            }

            return new UsernamePasswordAuthenticationToken(userId, null, List.of());
        } catch (Exception ex) {
            log.warn("Invalid token: {}", token);
            return null;
        }
    }

}
