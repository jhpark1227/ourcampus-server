package com.umc.ourcampus.auth.domain;

public record UserPrincipal(
        Role role,
        long memberId,
        long universityId
) {
}
