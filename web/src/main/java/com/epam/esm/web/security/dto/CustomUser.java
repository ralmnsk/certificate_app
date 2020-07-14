package com.epam.esm.web.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class CustomUser extends User {
    private static final long serialVersionUID = 1L;

    public CustomUser(UserRegistrationDto user) {
        super(user.getLogin(), user.getPassword(), user.getGrantedAuthoritiesList());
    }
}
