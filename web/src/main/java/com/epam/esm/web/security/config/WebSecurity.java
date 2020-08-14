package com.epam.esm.web.security.config;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.AccessException;
import com.epam.esm.model.Role;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
public class WebSecurity {
    private UserService userService;
    private static final String ACCESS_DENIED = "Access denied";
    private static final String USER = "User with principal login: ";
    private static final String EXCEPTION = " not found exception";

    public WebSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean checkUserId(String login, Long userId) {

        UserDto userDto = userService.findByLogin(login);
        if (userDto == null) {
            log.warn(ACCESS_DENIED);
            throw new AccessException(ACCESS_DENIED);
        }
        if (userDto.getRole().equals(Role.ADMIN)) {
            return true;
        }

        if (userDto.getId().equals(userId)) {
            return true;
        }
        throw new AccessException(ACCESS_DENIED);
    }

    public boolean checkUserId(Principal principal, Long userId) {
        String login = principal.getName();
        return checkUserId(login, userId);
    }

    public boolean checkOrderId(String login, Long orderId) {

        UserDto userFromLogin = userService.findByLogin(login);
        UserDto userDto = userService.getUserByOrderId(orderId).orElseThrow(() -> new NotFoundException(USER + login + EXCEPTION));
        if (userFromLogin.getRole().equals(Role.ADMIN)) {
            return true;
        }
        if (userDto.getLogin().equals(login)) {
            return true;
        }
        throw new AccessException(ACCESS_DENIED);
    }

    public boolean checkOrderId(Principal principal, Long orderId) {
        String login = principal.getName();
        return checkOrderId(login, orderId);
    }

    public boolean checkOperationAccess(String login) {

        UserDto userDto = userService.findByLogin(login);
        if (userDto == null) {
            log.warn(USER + login + EXCEPTION);
            throw new NotFoundException(USER + login + EXCEPTION);
        }
        if (userDto.getRole().equals(Role.ADMIN)) {
            return true;
        }
        log.warn(ACCESS_DENIED);
        throw new AccessException(ACCESS_DENIED);
    }

    public boolean checkOperationAccess(Principal principal) {
        String login = principal.getName();
        return checkOperationAccess(login);
    }
}
