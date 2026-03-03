package com.umc.ourcampus.notice.application.dto.request;

import com.umc.ourcampus.notice.domain.NoticeType;

public record UpdateNoticeRequest(
        String title,
        String content,
        NoticeType type
) {
}
