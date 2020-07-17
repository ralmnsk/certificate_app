package com.epam.esm.web.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.AccessException;
import com.epam.esm.service.user.UserService;
import com.epam.esm.service.dto.security.LoginDto;
import com.epam.esm.service.dto.security.RegistrationDto;
import com.epam.esm.web.security.jwt.JwtTokenProvider;
import com.epam.esm.service.security.OAuthService;
import com.epam.esm.service.security.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/")
public class GateController {

    private JwtTokenProvider jwtTokenProvider;
    private OAuthService oAuthService;
    private BCryptPasswordEncoder passwordEncoder;
    private RegistrationService registrationService;
    private UserService userService;

    public GateController(JwtTokenProvider jwtTokenProvider, OAuthService oAuthService,
                          BCryptPasswordEncoder passwordEncoder, RegistrationService registrationService,
                          UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuthService = oAuthService;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String login = loginDto.getLogin();
            RegistrationDto userDetails = oAuthService.getUserDetails(login);

            if (userDetails == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }
            if (passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails.getLogin(), userDetails.getGrantedAuthoritiesList()));//GOLDEN CODE
                UserDto userDto = userService.findByLogin(login);
                String token = jwtTokenProvider.createToken(login, userDto.getName());

                Map<Object, Object> response = new HashMap<>();
                response.put("username", login);
                response.put("token", token);
                return ResponseEntity.ok(response);
            }

        } catch (Exception ex) {
            log.info("invalid username/password," + ex.getMessage());
            throw new AccessException("Invalid username or password");
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/register")
    public RegistrationDto register(@Valid @RequestBody RegistrationDto registrationDto) {
        RegistrationDto registeredUser = registrationService.register(registrationDto);
        return registeredUser;
    }

//    @GetMapping("/login/oauth2/code/google")
//    public void getInfo(HttpServletRequest req, HttpServletResponse resp){
//        System.out.println(req);
//        System.out.println(resp);
//    }
}