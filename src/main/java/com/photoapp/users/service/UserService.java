package com.photoapp.users.service;

import com.photoapp.users.data.UserRepository;
import com.photoapp.users.mapper.UserMapper;
import com.photoapp.users.shared.AppUserDetails;
import com.photoapp.users.shared.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto userDto) {
        var userEntity = userMapper.userDtoToUserEntity(userDto)
            .toBuilder()
            .userId(UUID.randomUUID().toString())
            .encryptedPassword(passwordEncoder.encode(userDto.getPassword()))
            .build();

        userRepository.save(userEntity);

        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist!");
        }

        return new AppUserDetails(user);
    }
}
