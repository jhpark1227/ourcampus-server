package com.umc.ourcampus.auth.presentation;

import com.umc.ourcampus.auth.domain.LoginTokenIssuer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final LoginTokenIssuer loginTokenIssuer;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // OPTIONS 요청은 CORS preflight이므로 필터 건너뜀
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        // 토큰이 있으면 검증 후 SecurityContext에 설정
        if (accessToken != null) {
            try {
                loginTokenIssuer.validate(accessToken);
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken));
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 인증 없이 진행 (SecurityConfig에서 차단됨)
                log.warn("Invalid JWT token: {}", e.getMessage());
            }
        }
        // 토큰이 없거나 유효하지 않아도 다음 필터로 진행
        // SecurityConfig의 authorizeHttpRequests()에서 인증 필요 여부 체크
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
        return new UsernamePasswordAuthenticationToken(
                loginTokenIssuer.getMemberPrincipal(token),
                "",
                new HashSet<>()
        );
    }
}
