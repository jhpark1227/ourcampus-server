package com.umc.ourcampus.auth.domain;

public record MemberPrincipal(
        long memberId,
        long universityId
) {
}
