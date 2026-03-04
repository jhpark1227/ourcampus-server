package com.umc.ourcampus.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    static final String JWT_ERROR_STATUS_ATTRIBUTE = "JWT_ERROR_STATUS";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        ErrorStatus errorStatus = (ErrorStatus) request.getAttribute(JWT_ERROR_STATUS_ATTRIBUTE);
        if (errorStatus == null) {
            errorStatus = ErrorStatus.BAD_JWT;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatus.getHttpStatus().value());
        objectMapper.writeValue(response.getWriter(), ErrorResponse.from(errorStatus));
    }
}