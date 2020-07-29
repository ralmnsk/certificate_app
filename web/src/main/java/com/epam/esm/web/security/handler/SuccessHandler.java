package com.epam.esm.web.security.handler;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Role;
import com.epam.esm.repository.exception.SaveException;
import com.epam.esm.service.UserService;
import com.epam.esm.web.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String SURNAME = "family_name";
    private static final String NO_ENOUGH_INFORMATION = "Provider can't provide need information for login";
    private static final String EMPTY = "";

    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @Autowired
    public SuccessHandler(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Map<String, Object> attr = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();

        String login = (String) attr.get(EMAIL);
        String name = (String) attr.get(NAME);
        String surname = (String) attr.get(SURNAME);

        if (name.contains(surname)) {
            name = (name.replace(surname, EMPTY)).trim();
        }

        if ((login == null && name == null)) {
            response.getWriter().write(NO_ENOUGH_INFORMATION);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            UserDto user = getUser(login, name, surname);
            String token = jwtTokenProvider.createToken(user.getLogin(), user.getName());

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            response.addHeader("Authorization", "Bearer_" + token);

            Map<Object, Object> responseObj = new HashMap<>();
            responseObj.put("token", "Bearer_" + token);
            responseObj.put("user_link:", getURLBase(request) + "/users/" + user.getId());
            response.setStatus(HttpStatus.OK.value());
            String json = new ObjectMapper().writeValueAsString(responseObj);
            response.getWriter().write(json);
            response.flushBuffer();
        }
    }

    private UserDto getUser(String login, String name, String surname) {
        UserDto user = new UserDto();
        login = (login == null ? name : login);
        user.setLogin(login);
        user.setPassword(new BCryptPasswordEncoder().encode(""));
        user.setName(name);
        user.setSurname(surname);
        user.setRole(Role.USER);
        user.setDeleted(false);

        try {
            user = userService.save(user).orElseThrow(() -> new SaveException("User already exists save exception"));
        } catch (SaveException e) {
            user = userService.findByLogin(login);
        }

        return user;
    }

    public String getURLBase(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;

    }


}
