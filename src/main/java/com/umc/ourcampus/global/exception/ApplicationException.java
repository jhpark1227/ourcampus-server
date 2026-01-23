package com.umc.ourcampus.global.exception;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public ApplicationException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
