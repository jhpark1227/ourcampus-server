package com.umc.ourcampus.auth.application;

import com.umc.ourcampus.auth.domain.EmailMessage;

public interface EmailSender {
    void send(EmailMessage message);
}
