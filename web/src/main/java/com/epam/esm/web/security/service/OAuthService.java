package com.epam.esm.web.security.service;

import com.epam.esm.model.Role;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.security.dto.RegistrationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class OAuthService {

    private UserService userService;

    public OAuthService(UserService userService) {
        this.userService = userService;
    }

    public RegistrationDto getUserDetails(String login) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();

        RegistrationDto registrationDto = null;
        UserDto userDto = userService.findByLogin(login);

        if (userDto != null) {
            registrationDto = new RegistrationDto();
            registrationDto.setPassword(userDto.getPassword());
            registrationDto.setLogin(userDto.getLogin());
            registrationDto.setSurname(userDto.getSurname());
            registrationDto.setName(userDto.getName());

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_GUEST");
            if (userDto.getRole().equals(Role.USER)) {
                authority = new SimpleGrantedAuthority("ROLE_USER");
            } else if (userDto.getRole().equals(Role.ADMIN)) {
                authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            }
            registrationDto.setGrantedAuthoritiesList(Arrays.asList(authority));

        }

        return registrationDto;
    }
}