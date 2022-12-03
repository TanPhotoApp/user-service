package com.photoapp.user.security;

import com.photoapp.user.service.UserService;
import org.springframework.context.annotation.Bean;
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
    public LoginAuthenticationFilter loginAuthenticationFilter(AuthenticationManager authenticationManager,
                                                               TokenProperties tokenProperties) {
        return new LoginAuthenticationFilter(authenticationManager, tokenProperties);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginAuthenticationFilter loginAuthenticationFilter) throws Exception {
        http
            .csrf().disable()
            .headers()
                .frameOptions().disable()
            .and()
            .authorizeRequests()
                .antMatchers(
                    "/users/**",
                    "/actuator/**",
                    "/h2-console"
                ).permitAll()
                .anyRequest().denyAll()
            .and()
            .addFilter(loginAuthenticationFilter);

        return http.build();
    }

}
