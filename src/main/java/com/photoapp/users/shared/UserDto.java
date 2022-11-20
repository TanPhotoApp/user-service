package com.photoapp.users.shared;

import lombok.Data;

@Data
public class UserDto {

    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;

}
