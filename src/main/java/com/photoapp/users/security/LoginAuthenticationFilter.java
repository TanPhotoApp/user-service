package com.photoapp.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photoapp.users.api.model.LoginRequest;
import com.photoapp.users.shared.AppUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Don't define this filter as a Spring bean to avoid circular dependency injection. Because SecurityConfiguration
 * requires this bean, but this bean require AuthenticationManager which is defined in SecurityConfiguration
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProperties tokenProperties;

    /**
     * NOTE: require super.setAuthenticationManager to register a custom of UsernamePasswordAuthenticationFilter
     * Otherwise, throw error: authenticationManager must be specified
     */
    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, TokenProperties tokenProperties) {
        super.setAuthenticationManager(authenticationManager);
        super.setFilterProcessesUrl("/users/login");
        this.tokenProperties = tokenProperties;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

        return this.getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(
                credential.getEmail(),
                credential.getPassword(),
                List.of()
            )
        );
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        AppUserDetails userDetail = (AppUserDetails) authResult.getPrincipal();

        var key = Keys.hmacShaKeyFor(tokenProperties.getSigningKey().getBytes(StandardCharsets.UTF_8));
        var token = Jwts.builder()
            .setSubject(userDetail.getUserId())
            .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpireTime()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        response.addHeader("token", token);
        response.addHeader("user-id", userDetail.getUserId());
    }

}
