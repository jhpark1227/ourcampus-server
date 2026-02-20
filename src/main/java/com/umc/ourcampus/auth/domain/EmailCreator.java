package com.umc.ourcampus.auth.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailCreator {

    private final String fromEmail;
    private final String fromDisplayName;

    public EmailCreator(
            @Value("${mail.from.address}") String fromEmail,
            @Value("${mail.from.display-name}") String fromDisplayName
    ) {
        this.fromEmail = fromEmail;
        this.fromDisplayName = fromDisplayName;
    }

    public EmailMessage createVerificationMessage(EmailVerification verification) {
        String from = formatFrom(fromEmail, fromDisplayName);
        String subject = "이메일 인증 코드";
        String contentFormat = "인증 코드: %s\n\n인증 코드를 입력하여 이메일 인증을 완료해주세요.";
        return new EmailMessage(
                from,
                verification.getEmail().address(),
                subject,
                contentFormat.formatted(verification.getCode())
        );
    }

    private String formatFrom(String email, String displayName) {
        return String.format("%s <%s>", displayName, email);
    }
}