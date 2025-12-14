package com.example.school.auth.domain;

public record MemberPrincipal(
        Long memberId,
        Long universityId
) {
}
