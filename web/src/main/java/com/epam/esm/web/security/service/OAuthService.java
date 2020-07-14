package com.epam.esm.web.security.service;

import com.epam.esm.model.Role;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.security.dto.UserRegistrationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class OAuthService {

    private UserService<UserDto, Long, UserFilterDto> userService;

    public OAuthService(UserService<UserDto, Long, UserFilterDto> userService) {
        this.userService = userService;
    }

    public UserRegistrationDto getUserDetails(String login) {
        Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();

        UserRegistrationDto userRegistrationDto = null;
        UserDto userDto = userService.findByLogin(login);

        if (userDto != null) {
            userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setPassword(userDto.getPassword());
            userRegistrationDto.setLogin(userDto.getLogin());
            userRegistrationDto.setSurname(userDto.getSurname());
            userRegistrationDto.setName(userDto.getName());

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_GUEST");
            if (userDto.getRole().equals(Role.USER)) {
                authority = new SimpleGrantedAuthority("ROLE_USER");
            } else if (userDto.getRole().equals(Role.ADMIN)) {
                authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            }
            userRegistrationDto.setGrantedAuthoritiesList(Arrays.asList(authority));

        }

        return userRegistrationDto;
    }
}