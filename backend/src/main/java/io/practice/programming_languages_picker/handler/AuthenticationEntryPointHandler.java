package io.practice.programming_languages_picker.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.practice.programming_languages_picker.model.ServerError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        // Send a custom error response
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        String exceptionMsg = (String)  request.getAttribute("exception_message");
        System.out.println(authException.getClass());
        response.getWriter().write(objectMapper.writeValueAsString(new ServerError(exceptionMsg != null && !exceptionMsg.isBlank() ?  exceptionMsg : authException.getMessage(), HttpStatus.UNAUTHORIZED)));
    }
}
