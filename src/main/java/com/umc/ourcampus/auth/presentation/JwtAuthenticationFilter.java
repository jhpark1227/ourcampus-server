package com.umc.ourcampus.auth.presentation;

import com.umc.ourcampus.auth.domain.LoginTokenIssuer;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final LoginTokenIssuer loginTokenIssuer;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        return requestUri.equals("/auth/login") ||
                requestUri.equals("/auth/reissue") ||
                requestUri.startsWith("/auth/password") ||
                requestUri.startsWith("/members/password") ||
                requestUri.startsWith("/members/register") ||
                requestUri.equals("/members/find-email") ||
                requestUri.equals("/universities") ||
                requestUri.matches("/universities/.*/departments");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = extractAccessToken(request);
            loginTokenIssuer.validate(accessToken);
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken));
        } catch (Exception e) {
            handleAuthenticationError(response, e);
            log.warn(e.getMessage(), e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApplicationException(ErrorStatus.BAD_JWT);
        }
        return authorizationHeader.substring("Bearer ".length());

    }

    private void handleAuthenticationError(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.from(exception.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }

    private Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(
                loginTokenIssuer.getMemberPrincipal(token),
                "",
                new HashSet<>()
        );
    }
}
