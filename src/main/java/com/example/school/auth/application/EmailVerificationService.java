package com.example.school.auth.application;

import com.example.school.auth.application.dto.request.CodeVerifyRequest;
import com.example.school.auth.application.dto.request.VerificationEmailRequest;
import com.example.school.auth.application.dto.response.VerificationResponse;
import com.example.school.auth.domain.EmailCreator;
import com.example.school.auth.domain.EmailMessage;
import com.example.school.auth.domain.EmailVerification;
import com.example.school.auth.domain.EmailVerificationRepository;
import com.example.school.auth.domain.VerificationTokenIssuer;
import com.example.school.auth.domain.VerificationType;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Email;
import com.example.school.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final MemberRepository memberRepository;
    private final EmailCreator emailCreator;
    private final EmailSender emailSender;
    private final VerificationTokenIssuer VerificationTokenIssuer;

    public void sendRegisterVerificationEmail(VerificationEmailRequest request) {
        Email email = new Email(request.email());
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new ApplicationException(ErrorStatus.MEMBER_ALREADY_EXIST);
                });
        emailVerificationRepository.findTopByEmailAndTypeOrderByCreatedAtDesc(email, VerificationType.REGISTER)
                .ifPresent(EmailVerification::validateWithinCooldown);
        EmailVerification emailVerification = new EmailVerification(email, VerificationType.REGISTER);
        EmailMessage emailMessage = emailCreator.createVerificationMessage(emailVerification);
        emailSender.send(emailMessage);
        emailVerificationRepository.save(emailVerification);
    }

    public VerificationResponse verifyRegisterCode(CodeVerifyRequest request) {
        Email email = new Email(request.email());
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByEmailAndTypeOrderByCreatedAtDesc(email, VerificationType.REGISTER)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.VERIFICATION_NOT_FOUND));

        emailVerification.verify(request.code());
        emailVerificationRepository.save(emailVerification);
        String token = VerificationTokenIssuer.issue(email, VerificationType.REGISTER);
        return new VerificationResponse(token);
    }

    public void sendPasswordChangeVerificationEmail(VerificationEmailRequest request) {
        Email email = new Email(request.email());
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        emailVerificationRepository.findTopByEmailAndTypeOrderByCreatedAtDesc(email, VerificationType.PASSWORD_RESET)
                .ifPresent(EmailVerification::validateWithinCooldown);
        EmailVerification emailVerification = new EmailVerification(email, VerificationType.PASSWORD_RESET);
        EmailMessage emailMessage = emailCreator.createVerificationMessage(emailVerification);
        emailSender.send(emailMessage);
        emailVerificationRepository.save(emailVerification);
    }

    public VerificationResponse verifyPasswordChangeCode(CodeVerifyRequest request) {
        Email email = new Email(request.email());
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByEmailAndTypeOrderByCreatedAtDesc(email, VerificationType.PASSWORD_RESET)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.VERIFICATION_NOT_FOUND));

        emailVerification.verify(request.code());
        emailVerificationRepository.save(emailVerification);
        String token = VerificationTokenIssuer.issue(email, VerificationType.PASSWORD_RESET);
        return new VerificationResponse(token);
    }
}
