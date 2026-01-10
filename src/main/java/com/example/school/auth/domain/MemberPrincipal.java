package com.example.school.auth.domain;

public record MemberPrincipal(
        long memberId,
        long universityId
) {
}
