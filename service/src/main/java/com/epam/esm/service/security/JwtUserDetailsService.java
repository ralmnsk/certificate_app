package com.epam.esm.service.security;

import com.epam.esm.dto.security.CustomUserDetails;
import com.epam.esm.dto.security.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
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
            log.warn(e.getMessage());
            return null;
        }
    }
}
