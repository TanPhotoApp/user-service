package com.photoapp.user.service;

import com.photoapp.user.data.AlbumServiceClient;
import com.photoapp.user.data.UserRepository;
import com.photoapp.user.mapper.UserMapper;
import com.photoapp.user.shared.AppUserDetails;
import com.photoapp.user.shared.UserDto;
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
//    private final RestTemplate restTemplate;
    private final AlbumServiceClient albumServiceClient;

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

    public UserDto getUserByUserId(String userId) {
        var userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        var userDto = userMapper.userEntityToUserDto(userEntity);

        // call album-service by RestTemplate
//        String url = "http://album-service/users/%s/albums".formatted(userId);
//        var albums = List.of(restTemplate.getForObject(url, AlbumResponse[].class));

        var albums = albumServiceClient.getAlbums(userId);
        userDto.setAlbums(albums);

        return userDto;
    }

}
