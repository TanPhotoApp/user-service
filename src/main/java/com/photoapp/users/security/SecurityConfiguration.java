package com.photoapp.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Since spring 2.7, the WebSecurityConfigurerAdapter is deprecated. We have to build Security configuration by ourselves
 */
@EnableWebSecurity
public class SecurityConfiguration {

//    @Bean
//    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user")
//            .password(bCryptPasswordEncoder.encode("userPass"))
//            .roles("USER")
//            .build());
//        manager.createUser(User.withUsername("admin")
//            .password(bCryptPasswordEncoder.encode("adminPass"))
//            .roles("USER", "ADMIN")
//            .build());
//        return manager;
//    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
//        throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(bCryptPasswordEncoder)
//            .and()
//            .build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers()
                .frameOptions().disable() // To make h2-console work properly, because h2-console use frameOption
            .and()
            .authorizeRequests()
                .antMatchers("/users/**").permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
