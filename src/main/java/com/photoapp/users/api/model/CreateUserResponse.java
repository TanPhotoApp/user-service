package com.photoapp.users.api.model;

import lombok.Data;

@Data
public class CreateUserResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

}
