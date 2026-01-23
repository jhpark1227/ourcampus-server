package com.umc.ourcampus.member.application.dto.response;

import com.umc.ourcampus.member.domain.Inquiry;
import com.umc.ourcampus.member.domain.InquiryStatus;
import java.time.LocalDateTime;

public record InquiryResponse(
        long id,
        String title,
        String content,
        InquiryStatus status,
        LocalDateTime createdAt
) {
    public static InquiryResponse from(Inquiry inquiry) {
        return new InquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getCreatedAt()
        );
    }
}
