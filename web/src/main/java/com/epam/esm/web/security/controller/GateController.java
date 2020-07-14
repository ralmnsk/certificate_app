package com.epam.esm.web.security.controller;

import com.epam.esm.web.security.dto.LoginDto;
import com.epam.esm.web.security.dto.RegistrationDto;
import com.epam.esm.web.security.jwt.JwtTokenProvider;
import com.epam.esm.web.security.service.OAuthService;
import com.epam.esm.web.security.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/")
public class GateController {

    private JwtTokenProvider jwtTokenProvider;
//    private AuthenticationManager authenticationManager;
    private OAuthService oAuthService;
    private BCryptPasswordEncoder passwordEncoder;
    private RegistrationService registrationService;

    public GateController(JwtTokenProvider jwtTokenProvider, OAuthService oAuthService, BCryptPasswordEncoder passwordEncoder, RegistrationService registrationService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuthService = oAuthService;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String login = loginDto.getLogin();
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, userRegistrationDto.getPassword()));
            RegistrationDto userDetails = oAuthService.getUserDetails(login);

            if (userDetails == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }
            if(passwordEncoder.matches(loginDto.getPassword(),userDetails.getPassword())){
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails.getLogin(),
                                userDetails.getPassword(),userDetails.getGrantedAuthoritiesList()));//GOLDEN CODE
            String token = jwtTokenProvider.createToken(login, loginDto.getPassword());

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

    @PostMapping("/register")
    public RegistrationDto register(@Valid @RequestBody RegistrationDto registrationDto){
        RegistrationDto registeredUser = registrationService.register(registrationDto);
        return registeredUser;
    }
}