package com.umc.ourcampus.reservation.infrastructure;

import com.umc.ourcampus.member.domain.Password;
import com.umc.ourcampus.member.domain.PasswordEncoder;

public class PasswordEncoderStub implements PasswordEncoder {

    @Override
    public String encode(Password password) {
        return password.getValue();
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
