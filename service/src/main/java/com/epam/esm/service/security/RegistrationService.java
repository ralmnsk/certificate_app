package com.epam.esm.service.security;

import com.epam.esm.model.Role;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.user.UserService;
import com.epam.esm.service.dto.security.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private ModelMapper modelMapper;

    public RegistrationService(UserService userService, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public RegistrationDto register(RegistrationDto registrationUser) {
        UserDto userDto = new UserDto();
        userDto.setSurname(registrationUser.getSurname());
        userDto.setName(registrationUser.getName());
        userDto.setLogin(registrationUser.getLogin());
        userDto.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        userDto.setRole(Role.USER);

        userDto = userService.save(userDto).orElseThrow(() -> new SaveException("Registration User save exception, may be user already exists"));
        registrationUser = modelMapper.map(userDto, RegistrationDto.class);
        log.info("IN register - user: {} successfully registered", registrationUser);
        registrationUser.setPassword("[protected]");
        return registrationUser;
    }

}
