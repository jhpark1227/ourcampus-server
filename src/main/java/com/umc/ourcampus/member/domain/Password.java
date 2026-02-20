package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Password {

    private String value;

    public Password(String value) {
        if (!checkPassword(value)) {
            throw new ApplicationException(ErrorStatus.PASSWORD_FORMAT_ERROR);
        }
        this.value = value;
    }

    private boolean checkPassword(String password) {
        if (password.length() < 7 || password.length() > 15) {
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
}
