package com.example.school.member.application;

import com.example.school.member.domain.Password;

public interface PasswordEncoder {

    String encode(Password password);

    boolean matches(String rawPassword, String encodedPassword);
}
