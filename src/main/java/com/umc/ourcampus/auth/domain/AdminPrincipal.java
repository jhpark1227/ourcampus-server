package com.umc.ourcampus.auth.domain;

public record AdminPrincipal(
        long adminId,
        long universityId
) {
}