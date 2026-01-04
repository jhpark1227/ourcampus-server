package com.example.school.member.application;

public interface EmailSender {
    void sendVerificationEmail(String code);
}
