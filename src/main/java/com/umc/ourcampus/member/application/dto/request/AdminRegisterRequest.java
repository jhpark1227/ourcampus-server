package com.umc.ourcampus.member.application.dto.request;

public record AdminRegisterRequest(
        String loginId,
        String name,
        String password,
        long universityId
) {
}
