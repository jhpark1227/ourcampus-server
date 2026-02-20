package com.umc.ourcampus.global.apiPayload;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private ErrorStatus errorStatus;
}
