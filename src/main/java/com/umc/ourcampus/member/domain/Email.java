package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Email(
        @Column(name = "email")
        String address
) {
    public Email {
        if (!address.matches("^.+@.+$")) {
            throw new ApplicationException(ErrorStatus.EMAIL_FORMAT_ERROR);
        }
    }

    public String toMaskedAddress() {
        String[] parts = address.split("@");
        String id = parts[0];
        String domain = parts[1];
        if (id.length() <= 1) {
            return "*@" + domain;
        }
        if (id.length() <= 3) {
            return id.charAt(0) + "*".repeat(id.length() - 1) + "@" + domain;
        }
        String maskedPart = "*".repeat(id.length() - 3);
        return id.substring(0, 3) + maskedPart + "@" + domain;
    }
}
