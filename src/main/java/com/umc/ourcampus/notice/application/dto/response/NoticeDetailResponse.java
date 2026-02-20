package com.umc.ourcampus.notice.application.dto.response;

import com.umc.ourcampus.notice.domain.Notice;
import com.umc.ourcampus.notice.domain.NoticeType;
import java.time.LocalDate;

public record NoticeDetailResponse(
        Long id,
        String title,
        String content,
        NoticeType type,
        LocalDate date
) {
    public static NoticeDetailResponse from(Notice notice) {
        return new NoticeDetailResponse(
                notice.getId(), notice.getTitle(), notice.getContent(), notice.getType(),
                notice.getCreatedAt().toLocalDate()
        );
    }
}
