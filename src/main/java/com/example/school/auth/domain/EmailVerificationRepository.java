package com.example.school.auth.domain;

import com.example.school.member.domain.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(Email email);
    Optional<EmailVerification> findTopByEmailAndTypeOrderByCreatedAtDesc(Email email, VerificationType type);
}
