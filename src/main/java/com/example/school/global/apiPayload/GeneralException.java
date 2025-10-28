package com.example.school.global.apiPayload;

import com.example.school.global.apiPayload.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private ErrorStatus errorStatus;
}
