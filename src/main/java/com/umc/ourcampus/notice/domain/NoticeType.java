package com.umc.ourcampus.notice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType {
    GENERAL("일반"),
    RECRUIT("채용"),
    EVENT("행사");

    private final String displayName;
}
