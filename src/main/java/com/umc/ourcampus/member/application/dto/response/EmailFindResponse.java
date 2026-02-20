package com.umc.ourcampus.member.application.dto.response;

import com.umc.ourcampus.member.domain.Member;
import java.time.LocalDateTime;

public record EmailFindResponse(
        String email,
        LocalDateTime createdAt
) {
    public static EmailFindResponse from(Member member) {
        return new EmailFindResponse(member.getEmail().toMaskedAddress(), member.getCreatedAt());
    }
}
