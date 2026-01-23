package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.member.domain.Email;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE email_verification SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class EmailVerification extends BaseEntity {

    public static final Duration VERIFICATION_COOLDOWN = Duration.ofMinutes(1);
    private static final Duration VERIFICATION_TIME_LIMIT = Duration.ofMinutes(10);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Email email;

    private String code;

    @Enumerated(EnumType.STRING)
    private VerificationType type;

    public EmailVerification(Email email, VerificationType type) {
        this.email = email;
        this.type = type;
        this.code = createVerificationCode();
    }

    private String createVerificationCode() {
        return RandomGeneratorFactory.getDefault().create()
                .ints(4, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    public void validateWithinCooldown() {
        if (createdAt.plus(VERIFICATION_COOLDOWN).isAfter(LocalDateTime.now())) {
            throw new ApplicationException(ErrorStatus.EMAIL_COOL_TIME);
        }
    }

    public void verify(String code) {
        if (!this.code.equals(code)) {
            throw new ApplicationException(ErrorStatus.WRONG_EMAIL_CODE);
        }
        if (createdAt.plus(VERIFICATION_TIME_LIMIT).isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorStatus.EXPIRED_EMAIL_CODE);
        }
    }
}
