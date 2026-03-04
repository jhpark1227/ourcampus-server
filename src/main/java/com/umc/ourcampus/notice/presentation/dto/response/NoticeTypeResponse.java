package com.umc.ourcampus.notice.presentation.dto.response;

import com.umc.ourcampus.notice.domain.NoticeType;

public record NoticeTypeResponse(
        String name,
        String displayName
) {
    public static NoticeTypeResponse from(NoticeType type) {
        return new NoticeTypeResponse(type.name(), type.getDisplayName());
    }
}
