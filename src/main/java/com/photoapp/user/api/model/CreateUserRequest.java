package com.photoapp.user.api.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {

    @NotNull
    @Size(min = 2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotNull
    @Size(min = 2, message = "Last name must be at least 2 characters")
    private String lastName;

    @NotBlank
    @Size(min = 8, max = 16, message = "Password must be at least 8 characters and less than 16 characters")
    private String password;

    @NotNull
    @Email
    private String email;

}
