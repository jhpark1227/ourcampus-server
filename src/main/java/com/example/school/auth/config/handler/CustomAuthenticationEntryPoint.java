package com.example.school.auth.config.handler;

import com.example.school.global.apiPayload.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        log.info("exception: " + exception);

        // 에러 응답 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        /**
         * 토큰 없는 경우
         */
        if (exception == null) {
            log.info("토큰 없는 경우");
            response.getWriter().write("{'error': '토큰이 없습니다.'}");
            return;
        }

        /**
         * 토큰 만료된 경우
         */
        if (exception.equals(ErrorStatus.EXPIRED_JWT.getMessage())) {
            log.info("토큰이 만료된 경우 !!!");
            response.getWriter().write("{'error': '토큰이 만료되었습니다.'}");
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if (exception.equals(ErrorStatus.BAD_JWT.getMessage())) {
            log.info("이상한 토큰이 들어왔습니다. !!!");
            response.getWriter().write("{'error': '유효하지 않은 토큰입니다.'}");
        }
    }
}
