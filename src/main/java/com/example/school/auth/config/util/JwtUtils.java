package com.example.school.auth.config.util;

import com.example.school.auth.application.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class JwtUtils {

    private final String SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTcwNjAxMDkwOSwiaWF0IjoxNzA2MDEwOTA5fQ.sZZKJaY_DQ-_LTVgcOQ44GrqaOH_9GgboZd85YkgsMM";

    public static final String REFRESH_TOKEN_NAME = "refresh_token";
//    @Value("${jwt.token-valid-time}")
//    public static long TOKEN_VALID_TIME;
//    @Value("${jwt.refresh-token-valid-time}")
//    public static long REFRESH_TOKEN_VALID_TIME;
//    @Value("${jwt.refresh-token-valid-time-in-redis}")
//    public static long REFRESH_TOKEN_VALID_TIME_IN_REDIS;

    public static final long TOKEN_VALID_TIME = 1000L * 120 * 5 * 12; // 2시간
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144;
    public static final long REFRESH_TOKEN_VALID_TIME_IN_REDIS = 60 * 60 * 24 * 7;
    private final UserDetailsServiceImpl userDetailsService;


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

    // 권한정보 획득
    // Spring Security 인증과정에서 권한 확인을 위해 사용
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmailInToken(token));

        if (userDetails != null) {
            log.info("userDetails.getUsername : " + userDetails.getUsername());
            log.info("userDetails.getPassword : " + userDetails.getPassword());

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            if (authorities != null) {
                log.info("getAuthorities() : " + authorities.toString());
            } else {
                log.warn("getAuthorities() returned null");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } else {
            log.warn("UserDetails is null");
            // 처리할 내용 추가 (예: 예외 발생 또는 기본 권한 부여 등)
            return null;
        }
    }

    // username(id)를 claim에 넣어서 사용할경우 보안상 좋지 않음 대체할것이 필요
    // username -> email 으로 변경
    public String createToken(String email, long expireTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    //
    public String resolveToken(String token) {
        if (token != null) {
            return token.substring("Bearer ".length());
        } else {
            return "";
        }
    }

    public boolean validateToken(String jwtToken) {
        log.info("Enter the validateToken Method");
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSigningKey(SECRET_KEY)).build()
                .parseClaimsJws(jwtToken);
        log.info("email : " + claims.getBody().get("email"));

        return !claims.getBody().getExpiration().before(new Date());
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey(SECRET_KEY)).build().parseClaimsJws(token).getBody();
    }

    public Long getExpiration(String jwtToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(getSigningKey(SECRET_KEY)).build()
                .parseClaimsJws(jwtToken).getBody().getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

//    public boolean getReportedStatusByToken(String token) {
//        Member member = authRepository.findByEmail(getEmailInToken(token)).orElseThrow(() -> {
//            throw new RuntimeException("error");
//        });
//
//        log.info("user Reported Status : " + member.getReport().toString());
//        if (member.getReportedStatus().equals(ReportStatus.ACCOUNT_SUSPENDED)) {
//            log.info("Reported Status bad");
//            return false;
//        } else {
//            log.info("Reported Status good");
//            return true;
//        }
//    }


}
