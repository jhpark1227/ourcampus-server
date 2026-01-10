package com.example.school.auth.infrastructure;

import com.example.school.auth.application.EmailSender;
import com.example.school.auth.domain.EmailMessage;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaEmailSender implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void send(EmailMessage emailMessage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailMessage.from());
            helper.setTo(emailMessage.to());
            helper.setSubject(emailMessage.subject());
            helper.setText(emailMessage.content());

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new ApplicationException(ErrorStatus.EMAIL_SEND_ERROR);
        }
    }
}
