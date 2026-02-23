package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.auth.infrastructure.JwtProvider;
import com.umc.ourcampus.member.domain.Admin;
import com.umc.ourcampus.member.domain.Member;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginTokenIssuer {

    private final Duration ACCESS_TOKEN_VALID_TIME;
    private final Duration REFRESH_TOKEN_VALID_TIME;

    private final JwtProvider jwtProvider;

    public LoginTokenIssuer(
            @Value("${auth.jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes,
            @Value("${auth.jwt.refresh-token-expiration-minutes}") long refreshTokenExpirationMinutes,
            JwtProvider jwtProvider
    ) {
        this.ACCESS_TOKEN_VALID_TIME = Duration.ofMinutes(accessTokenExpirationMinutes);
        this.REFRESH_TOKEN_VALID_TIME = Duration.ofMinutes(refreshTokenExpirationMinutes);
        this.jwtProvider = jwtProvider;
    }

    public TokenPair issueLoginTokenPair(Member member) {
        Map<String, String> claims = new HashMap<>();
        claims.put("memberId", String.valueOf(member.getId()));
        claims.put("universityId", String.valueOf(member.getUniversity().getId()));
        String accessToken = jwtProvider.createToken(claims, ACCESS_TOKEN_VALID_TIME);
        RefreshToken refreshToken = new RefreshToken(jwtProvider.createToken(claims, REFRESH_TOKEN_VALID_TIME), member);

        return new TokenPair(accessToken, refreshToken);
    }

    public String issueAdminToken(Admin admin) {
        Map<String, String> claims = new HashMap<>();
        claims.put("type", "ADMIN");
        claims.put("adminId", String.valueOf(admin.getId()));
        claims.put("universityId", String.valueOf(admin.getUniversity().getId()));
        return jwtProvider.createToken(claims, ACCESS_TOKEN_VALID_TIME);
    }

    public MemberPrincipal getMemberPrincipal(String token) {
        validate(token);
        Map<String, String> claims = jwtProvider.getClaims(token);
        return new MemberPrincipal(
                Long.parseLong(claims.get("memberId")),
                Long.parseLong(claims.get("universityId"))
        );
    }

    public AdminPrincipal getAdminPrincipal(String token) {
        validate(token);
        Map<String, String> claims = jwtProvider.getClaims(token);
        return new AdminPrincipal(
                Long.parseLong(claims.get("adminId")),
                Long.parseLong(claims.get("universityId"))
        );
    }

    public String getTokenType(String token) {
        return jwtProvider.getClaims(token).getOrDefault("type", "MEMBER");
    }

    public void validate(String token) {
        jwtProvider.validate(token);
    }
}
