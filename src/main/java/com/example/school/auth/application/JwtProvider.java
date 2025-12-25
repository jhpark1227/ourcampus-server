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
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public final class JwtProvider {

    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION_MILLIS;

    public JwtProvider(
            @Value("${auth.jwt.secret-key}") String secretKey,
            @Value("${auth.jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes
    ) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_EXPIRATION_MILLIS = TimeUnit.MINUTES.toMillis(accessTokenExpirationMinutes);
    }

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
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MILLIS))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member member) {
        return Jwts.builder()
                .claim("memberId", member.getId())
                .claim("universityId", member.getUniversity().getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MILLIS))
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
