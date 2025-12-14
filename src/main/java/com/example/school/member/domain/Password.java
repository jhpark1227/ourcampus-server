package com.example.school.member.domain;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
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
        if (checkPassword(value)) {
            throw new ApplicationException(ErrorStatus.PASSWORD_FORMAT_ERROR);
        }
        this.value = value;
    }

    private Boolean checkPassword(String password) {
        if (password.length() < 8 || password.length() > 15) {
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
}
