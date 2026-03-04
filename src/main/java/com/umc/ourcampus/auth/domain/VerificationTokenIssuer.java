package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.auth.infrastructure.JwtProvider;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.Email;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenIssuer {

    private final Duration REGISTER_TOKEN_VALID_TIME;

    private final JwtProvider jwtProvider;

    public VerificationTokenIssuer(
            @Value("${auth.jwt.register-token-expiration-minutes}") long accessTokenExpirationMinutes,
            JwtProvider jwtProvider
    ) {
        this.REGISTER_TOKEN_VALID_TIME = Duration.ofMinutes(accessTokenExpirationMinutes);
        this.jwtProvider = jwtProvider;
    }

    public String issue(Email email, VerificationType type) {
        Map<String, String> claims = Map.of(
                "email", email.address(),
                "type", type.name()
        );
        return jwtProvider.createToken(claims, REGISTER_TOKEN_VALID_TIME);
    }

    public void validate(String token, Email email, VerificationType type) {
        jwtProvider.validate(token);
        Map<String, String> claims = jwtProvider.getClaims(token);
        if (!email.address().equals(claims.get("email"))) {
            throw new ApplicationException(ErrorStatus.WRONG_VERIFICATION_TOKEN);
        }
        if (!type.name().equals(claims.get("type"))) {
            throw new ApplicationException(ErrorStatus.WRONG_VERIFICATION_TOKEN);
        }

    }
}
