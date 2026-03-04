package com.umc.ourcampus.notice.application.dto.response;

import com.umc.ourcampus.notice.domain.Notice;
import com.umc.ourcampus.notice.domain.NoticeType;
import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        String title,
        String content,
        NoticeType type,
        LocalDate date
) {
    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(
                notice.getId(), notice.getTitle(), notice.getContent(), notice.getType(),
                notice.getCreatedAt().toLocalDate()
        );
    }
}
