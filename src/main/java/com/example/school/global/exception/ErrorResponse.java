package com.example.school.global.exception;

import com.example.school.global.apiPayload.status.ErrorStatus;
import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp
) {

    public static ErrorResponse from(ErrorStatus errorStatus) {
        return new ErrorResponse(errorStatus.getMessage(), LocalDateTime.now());
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse(message, LocalDateTime.now());
    }
}
