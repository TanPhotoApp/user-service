package com.photoapp.user.api.controller;

import com.photoapp.user.api.model.CreateUserRequest;
import com.photoapp.user.api.model.CreateUserResponse;
import com.photoapp.user.mapper.UserMapper;
import com.photoapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final Environment environment;
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port") + ", token secret = "
            + environment.getProperty("token.signingKey");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        var userDto = userMapper.createUserRequestToUserDto(request);
        var createdUserDto = userService.createUser(userDto);
        var response = userMapper.userDtoToCreateUserResponse(createdUserDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

}
