package com.umc.ourcampus.member.domain;

public interface PasswordEncoder {

    String encode(Password password);

    boolean matches(String rawPassword, String encodedPassword);
}
