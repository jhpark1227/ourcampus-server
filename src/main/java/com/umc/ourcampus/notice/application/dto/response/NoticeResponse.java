package com.umc.ourcampus.notice.application.dto.response;

import com.umc.ourcampus.notice.domain.Notice;
import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        String title,
        LocalDate date
) {
    public static NoticeResponse from(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getCreatedAt().toLocalDate());
    }
}
