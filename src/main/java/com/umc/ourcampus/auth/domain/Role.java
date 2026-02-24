package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import java.util.Arrays;

public enum Role {
    MEMBER, ADMIN;

    public static Role from(String value) {
        return Arrays.stream(values())
                .filter(role -> role.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BAD_JWT));
    }
}
