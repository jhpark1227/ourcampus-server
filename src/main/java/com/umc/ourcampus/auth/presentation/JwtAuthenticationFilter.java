package com.umc.ourcampus.auth.presentation;

import com.umc.ourcampus.auth.domain.LoginTokenIssuer;
import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.global.exception.ErrorStatus;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final LoginTokenIssuer loginTokenIssuer;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        if (accessToken != null) {
            try {
                loginTokenIssuer.validate(accessToken);
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken));
            } catch (ExpiredJwtException e) {
                log.warn("Expired JWT token: {}", e.getMessage());
                request.setAttribute(JwtAuthenticationEntryPoint.JWT_ERROR_STATUS_ATTRIBUTE, ErrorStatus.EXPIRED_JWT);
            } catch (Exception e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                request.setAttribute(JwtAuthenticationEntryPoint.JWT_ERROR_STATUS_ATTRIBUTE, ErrorStatus.BAD_JWT);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }

    private Authentication getAuthentication(String token) {
        UserPrincipal principal = loginTokenIssuer.getMemberPrincipal(token);
        return new UsernamePasswordAuthenticationToken(principal, "", List.of(new SimpleGrantedAuthority("ROLE_" + principal.role())));
    }
}
