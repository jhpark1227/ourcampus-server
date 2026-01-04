package com.example.school.member.infrastructure;

import com.example.school.member.application.EmailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderImpl implements EmailSender {

    @Override
    public void sendVerificationEmail(String code) {
        System.out.println(code);
    }
}
