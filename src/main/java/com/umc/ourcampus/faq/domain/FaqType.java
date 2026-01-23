package com.umc.ourcampus.faq.domain;

import lombok.Getter;

@Getter
public enum FaqType {

    RESERVATION("예약/반납"),
    JOIN("가입/탈퇴"),
    ETC("기타");

    private final String displayName;

    FaqType(String displayName) {
        this.displayName = displayName;
    }
}
