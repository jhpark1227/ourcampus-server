package com.example.school.auth.application;

import com.example.school.auth.domain.EmailMessage;

public interface EmailSender {
    void send(EmailMessage message);
}
