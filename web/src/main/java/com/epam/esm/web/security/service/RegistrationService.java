package com.epam.esm.web.security.service;

import com.epam.esm.model.Role;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.security.dto.UserRegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private UserService<UserDto, Long, UserFilterDto> userService;
    private ModelMapper modelMapper;

    public RegistrationService(UserService<UserDto, Long, UserFilterDto> userService, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserRegistrationDto register(UserRegistrationDto registrationUser) {
        UserDto userDto = new UserDto();
        userDto.setSurname(registrationUser.getSurname());
        userDto.setName(registrationUser.getName());
        userDto.setLogin(registrationUser.getLogin());
        userDto.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        userDto.setRole(Role.USER);

        userDto = userService.save(userDto).orElseThrow(() -> new SaveException("Registration User save exception, may be user already exists"));
        registrationUser = modelMapper.map(userDto, UserRegistrationDto.class);
        log.info("IN register - user: {} successfully registered", registrationUser);
        registrationUser.setPassword("[protected]");
        return registrationUser;
    }

}
