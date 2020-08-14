package com.epam.esm.service.security;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.security.RegistrationDto;
import com.epam.esm.model.Role;
import com.epam.esm.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OAuthService {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_GUEST = "ROLE_GUEST";

    private UserService userService;

    public OAuthService(UserService userService) {
        this.userService = userService;
    }

    public RegistrationDto getUserDetails(String login) {

        RegistrationDto registrationDto = null;
        UserDto userDto = userService.findByLogin(login);

        if (userDto != null) {
            registrationDto = new RegistrationDto();
            registrationDto.setPassword(userDto.getPassword());
            registrationDto.setLogin(userDto.getLogin());
            registrationDto.setSurname(userDto.getSurname());
            registrationDto.setName(userDto.getName());

            GrantedAuthority authority = new SimpleGrantedAuthority(ROLE_GUEST);
            if (userDto.getRole().equals(Role.USER)) {
                authority = new SimpleGrantedAuthority(ROLE_USER);
            } else if (userDto.getRole().equals(Role.ADMIN)) {
                authority = new SimpleGrantedAuthority(ROLE_ADMIN);
            }
            registrationDto.setGrantedAuthoritiesList(Arrays.asList(authority));

        }

        return registrationDto;
    }
}