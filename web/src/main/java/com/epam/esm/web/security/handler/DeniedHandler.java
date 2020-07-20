package com.epam.esm.web.security.handler;

import com.epam.esm.service.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
            throws IOException, ServletException {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto("Access denied", "Access denied for this resource");
        String json = new ObjectMapper().writeValueAsString(exceptionResponseDto);
        response.getWriter().write(json);
        response.flushBuffer();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
