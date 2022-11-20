package com.photoapp.users.api.controller;

import com.photoapp.users.api.model.CreateUserRequest;
import com.photoapp.users.api.model.CreateUserResponse;
import com.photoapp.users.mapper.UserMapper;
import com.photoapp.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {

    private final Environment environment;
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port");
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
