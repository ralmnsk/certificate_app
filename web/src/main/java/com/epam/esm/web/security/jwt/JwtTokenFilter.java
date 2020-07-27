package com.epam.esm.web.security.jwt;

import com.epam.esm.exception.JwtUserAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        try {

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.authentication(token);

                if (authentication == null) {
                    log.warn("User authentication exception");
                    throw new JwtUserAuthenticationException("User authentication exception");
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtUserAuthenticationException e) {
            setExceptionResponse(response, e, "User authentication exception. " + e.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }

    private void setExceptionResponse(ServletResponse response, Throwable e, String message) throws IOException {
        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN); //.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        Map<Object, Object> responseObj = new HashMap<>();
        responseObj.put("message:", message);
        responseObj.put("exception:", e.getClass());
        String json = new ObjectMapper().writeValueAsString(responseObj);
        response.getWriter().write(json);
        response.flushBuffer();
    }

    public String getURLBase(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
        return requestURL.getProtocol() + "://" + requestURL.getHost() + port;

    }

}
