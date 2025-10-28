package com.example.school.global.apiPayload;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<ApiResponse<Object>> onThrowException(GeneralException exception) {
        return new ResponseEntity<>(
                ApiResponse.onFailure(exception.getErrorStatus().getCode(), exception.getErrorStatus().getMessage()),
                exception.getErrorStatus().getHttpStatus());
    }
}
