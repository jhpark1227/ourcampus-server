package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.member.domain.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(Email email);
    Optional<EmailVerification> findTopByEmailAndTypeOrderByCreatedAtDesc(Email email, VerificationType type);
}
