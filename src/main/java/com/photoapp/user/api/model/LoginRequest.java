package com.photoapp.user.api.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
