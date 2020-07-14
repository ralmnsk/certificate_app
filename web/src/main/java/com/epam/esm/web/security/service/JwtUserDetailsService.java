package com.epam.esm.web.security.service;

import com.epam.esm.web.security.dto.CustomUserDetails;
import com.epam.esm.web.security.dto.RegistrationDto;
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
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        RegistrationDto registrationDto = null;
        try {
            registrationDto = oAuthService.getUserDetails(username);
            CustomUserDetails customUserDetails = new CustomUserDetails(registrationDto);
            return customUserDetails;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
    }
}
