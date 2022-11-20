package com.photoapp.users.service;

import com.photoapp.users.data.UserRepository;
import com.photoapp.users.mapper.UserMapper;
import com.photoapp.users.shared.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        var userEntity = userMapper.userDtoToUserEntity(userDto)
            .toBuilder()
            .userId(UUID.randomUUID().toString())
            .encryptedPassword("test")
            .build();

        userRepository.save(userEntity);

        return userMapper.userEntityToUserDto(userEntity);
    }

}
