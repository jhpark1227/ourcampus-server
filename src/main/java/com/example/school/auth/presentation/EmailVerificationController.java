package com.example.school.auth.presentation;

import com.example.school.auth.application.EmailVerificationService;
import com.example.school.auth.application.dto.request.CodeVerifyRequest;
import com.example.school.auth.application.dto.request.VerificationEmailRequest;
import com.example.school.auth.application.dto.response.VerificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/members/register/verification-code")
    public ResponseEntity<Void> sendRegisterVerificationEmail(@RequestBody @Valid VerificationEmailRequest request) {
        emailVerificationService.sendRegisterVerificationEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/register/verification-code/verify")
    public VerificationResponse verifyRegisterCode(@RequestBody @Valid CodeVerifyRequest request) {
        return emailVerificationService.verifyRegisterCode(request);
    }

    @PostMapping("/auth/password/verification-code")
    public ResponseEntity<Void> sendPasswordChangeVerificationEmail(@RequestBody @Valid VerificationEmailRequest request) {
        emailVerificationService.sendPasswordChangeVerificationEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/password/verification-code/verify")
    public VerificationResponse verifyPasswordChangeCode(@RequestBody @Valid CodeVerifyRequest request) {
        return emailVerificationService.verifyPasswordChangeCode(request);
    }
}
