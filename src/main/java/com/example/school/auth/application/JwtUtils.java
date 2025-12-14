package com.example.school.auth.application;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class JwtUtils {

    private static final String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwNjAxMDkwOSwiaWF0IjoxNzA2MDEwOTA5fQ.sZZKJaY_DQ-_LTVgcOQ44GrqaOH_9GgboZd85YkgsMM";
    private static final long TOKEN_VALID_TIME = 1000L * 120 * 5 * 12; // 2시간
    private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144;


    public Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public String getEmailInToken(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String createAccessToken(Member member) {
        return Jwts.builder()
                .claim("memberId", member.getId())
                .claim("universityId", member.getUniversity().getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member member) {
        return Jwts.builder()
                .claim("memberId", member.getId())
                .claim("universityId", member.getUniversity().getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(String token) {
        if (token != null) {
            return token.substring("Bearer ".length());
        } else {
            return "";
        }
    }

    public void validateToken(String jwtToken) {
        Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(jwtToken);
    }

    public MemberPrincipal getMemberPrincipal(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return new MemberPrincipal(claims.get("memberId", Long.class), claims.get("universityId", Long.class));
    }
}
