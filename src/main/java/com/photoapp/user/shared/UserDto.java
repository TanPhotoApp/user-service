package com.photoapp.user.shared;

import com.photoapp.user.api.model.AlbumResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;
    private List<AlbumResponse> albums;

}
