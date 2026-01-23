package com.umc.ourcampus.auth.infrastructure;

import java.time.Duration;
import java.util.Map;

public interface JwtProvider {
    String createToken(Map<String, String> claims, Duration validTime);

    void validate(String token);

    Map<String, String> getClaims(String token);
}
