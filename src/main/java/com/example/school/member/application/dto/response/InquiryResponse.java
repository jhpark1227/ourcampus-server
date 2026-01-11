package com.example.school.member.application.dto.response;

import com.example.school.member.domain.Inquiry;
import com.example.school.member.domain.InquiryStatus;
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
