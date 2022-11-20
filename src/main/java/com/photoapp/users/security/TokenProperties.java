package com.photoapp.users.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public record TokenProperties(long expireTime, String signingKey) {}
