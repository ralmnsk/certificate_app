package com.epam.esm.service.dto.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class CustomUserDetails extends User {
    private static final long serialVersionUID = 1L;

    public CustomUserDetails(RegistrationDto user) {
        super(user.getLogin(), user.getPassword(), user.getGrantedAuthoritiesList());
    }
}
