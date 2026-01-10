package com.example.school.global.exception;

import com.example.school.global.apiPayload.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(ErrorResponse.from(exception.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        log.warn(exception.getMessage(), exception);
        ErrorStatus errorStatus = exception.getErrorStatus();
        return ResponseEntity.status(errorStatus.getHttpStatus())
                .body(ErrorResponse.from(errorStatus));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from("서버 에러가 발생했습니다. 관리자에게 문의해주세요."));
    }
}
