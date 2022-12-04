package com.photoapp.user.security;

import com.photoapp.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, UserService userService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager,
                                                     TokenProperties tokenProperties) {
        return new AuthenticationFilter(authenticationManager, tokenProperties);
    }

    @Bean
    public AuthorizationFilter authorizationFilter(AuthenticationManager authenticationManager,
                                                   TokenProperties tokenProperties) {
        return new AuthorizationFilter(authenticationManager, tokenProperties);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationFilter authenticationFilter,
                                           AuthorizationFilter authorizationFilter) throws Exception {
        http
            .csrf().disable()
            .headers()
                .frameOptions().disable()
            .and()
            .authorizeRequests()
                // Simulate allow create users from API gateway only. TODO use oauth2 later
                .antMatchers(HttpMethod.POST, "/users").access("hasIpAddress(\"127.0.0.1\") or hasIpAddress(\"::1\")")
                .antMatchers("/actuator/**", "/h2-console").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilter(authenticationFilter)
            .addFilter(authorizationFilter);

        return http.build();
    }

}
