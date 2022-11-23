package com.photoapp.users.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private long expireTime;
    private String signingKey;

}
