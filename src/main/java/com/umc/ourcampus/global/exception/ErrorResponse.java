package com.umc.ourcampus.global.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp
) {

    public static ErrorResponse from(ErrorStatus errorStatus) {
        return new ErrorResponse(errorStatus.name(), errorStatus.getMessage(), LocalDateTime.now());
    }

    public static ErrorResponse from(String code, String message) {
        return new ErrorResponse(code, message, LocalDateTime.now());
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse("ERROR", message, LocalDateTime.now());
    }
}
