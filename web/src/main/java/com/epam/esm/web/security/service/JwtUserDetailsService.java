package com.epam.esm.web.security.service;

import com.epam.esm.web.security.dto.CustomUser;
import com.epam.esm.web.security.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("uds")
public class JwtUserDetailsService implements UserDetailsService {

    private OAuthService oAuthService;

    public JwtUserDetailsService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    public CustomUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserRegistrationDto userRegistrationDto = null;
        try {
            userRegistrationDto = oAuthService.getUserDetails(username);
            CustomUser customUser = new CustomUser(userRegistrationDto);
            return customUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
    }
}
