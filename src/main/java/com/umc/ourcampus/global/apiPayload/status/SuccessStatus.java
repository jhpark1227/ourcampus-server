package com.umc.ourcampus.global.apiPayload.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus {

    OK(HttpStatus.OK, "COMMON200", "성공입니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
