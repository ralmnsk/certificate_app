package com.epam.esm.web.security.config;

import com.epam.esm.model.Role;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.AccessException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Component
public class WebSecurity {
    private UserService userService;

    public WebSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean checkUserId(Principal principal, Long userId) {
        String login = principal.getName();
        UserDto userDto = userService.findByLogin(login);
        if (userDto == null) {
            throw new AccessException("Access denied");
        }
        if (userDto.getRole().equals(Role.ADMIN)) {
            return true;
        }

        if (userDto.getId().equals(userId)) {
            return true;
        }
        throw new AccessException("Access denied");
    }

    public boolean checkOrderId(Principal principal, Long orderId) {
        String login = principal.getName();
        UserDto userFromLogin = userService.findByLogin(login);
        UserDto userDto = userService.getUserByOrderId(orderId).orElseThrow(() -> new NotFoundException("User with principal login: " + login + " not found exception"));
        if (userFromLogin.getRole().equals(Role.ADMIN)) {
            return true;
        }
        if (userDto.getLogin().equals(login)) {
            return true;
        }
        throw new AccessException("Access denied");
    }

    public boolean checkOperationAccess(Principal principal) {
        String login = principal.getName();
        UserDto userDto = userService.findByLogin(login);
        if (userDto == null) {
            throw new NotFoundException("User with principal login: " + login + " not found exception");
        }
        if (userDto.getRole().equals(Role.ADMIN)) {
            return true;
        }
        throw new AccessException("Access denied");
    }
}
