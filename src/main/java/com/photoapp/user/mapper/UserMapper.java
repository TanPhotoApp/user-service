package com.photoapp.user.mapper;

import com.photoapp.user.api.model.CreateUserRequest;
import com.photoapp.user.api.model.CreateUserResponse;
import com.photoapp.user.api.model.UserResponse;
import com.photoapp.user.data.UserEntity;
import com.photoapp.user.shared.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto createUserRequestToUserDto(CreateUserRequest createUserRequest);

    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity userEntity);

    CreateUserResponse userDtoToCreateUserResponse(UserDto userDto);

    UserResponse userDtoToUserResponse(UserDto userDto);

}
