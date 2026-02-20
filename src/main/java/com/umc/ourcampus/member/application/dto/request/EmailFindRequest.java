package com.umc.ourcampus.member.application.dto.request;

public record EmailFindRequest(
        long universityId,
        String name,
        String studentId
) {
}
