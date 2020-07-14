package com.epam.esm.web.security.controller;

import com.epam.esm.web.security.dto.UserRegistrationDto;
import com.epam.esm.web.security.jwt.JwtTokenProvider;
import com.epam.esm.web.security.service.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class GateController {

    private JwtTokenProvider jwtTokenProvider;
//    private AuthenticationManager authenticationManager;
    private OAuthService oAuthService;
    private BCryptPasswordEncoder passwordEncoder;

    public GateController(JwtTokenProvider jwtTokenProvider, OAuthService oAuthService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuthService = oAuthService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserRegistrationDto userRegistrationDto) throws Exception {
        try {
            String login = userRegistrationDto.getLogin();
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, userRegistrationDto.getPassword()));
            UserRegistrationDto userDetails = oAuthService.getUserDetails(login);

            if (userDetails == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }
            if(passwordEncoder.matches(userRegistrationDto.getPassword(),userDetails.getPassword())){
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails.getLogin(),
                                userDetails.getPassword(),userDetails.getGrantedAuthoritiesList()));//GOLDEN CODE
            String token = jwtTokenProvider.createToken(login, userRegistrationDto.getPassword());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", login);
            response.put("token", token);
            return ResponseEntity.ok(response);
            }

        } catch (Exception ex) {
            log.info("invalid username/password");
        }
        return ResponseEntity.notFound().build();

    }
}