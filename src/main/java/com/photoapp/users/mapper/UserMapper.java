package com.photoapp.users.mapper;

import com.photoapp.users.api.model.CreateUserRequest;
import com.photoapp.users.api.model.CreateUserResponse;
import com.photoapp.users.data.UserEntity;
import com.photoapp.users.shared.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto createUserRequestToUserDto(CreateUserRequest createUserRequest);

    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity userEntity);

    CreateUserResponse userDtoToCreateUserResponse(UserDto userDto);

}
