package com.umc.ourcampus.auth.infrastructure;

import com.umc.ourcampus.auth.application.EmailSender;
import com.umc.ourcampus.auth.domain.EmailMessage;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
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
