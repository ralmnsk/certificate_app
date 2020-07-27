package com.epam.esm.web.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j

public class EntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpServletResponse resp = ((HttpServletResponse) response);
        String uri = req.getRequestURI();
//        if (((HttpServletResponse) response).getStatus() == 302) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<Object, Object> responseObj = new HashMap<>();
            responseObj.put("login", getURLBase(req) + "/login/oauth2/code/google");
            responseObj.put("register:", getURLBase(req) + "/register");
            String json = new ObjectMapper().writeValueAsString(responseObj);
            response.getWriter().write(json);
            response.flushBuffer();
//        }
    }

    public String getURLBase(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;

    }

}
