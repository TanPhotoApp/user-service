package com.photoapp.users.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "token")
@RefreshScope
public record TokenProperties(long expireTime, String signingKey) {}
