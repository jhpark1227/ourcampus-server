package com.umc.ourcampus.auth.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Email;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class EmailVerificationTest {

    @Test
    void 이메일_인증_쿨다운시간이_지나면_예외가_발생하지_않는다() {
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(2);
        EmailVerification emailVerification = new EmailVerification(new Email("email@domain.com"), VerificationType.REGISTER);
        setCreatedAt(emailVerification, createdAt);

        assertThatCode(() -> emailVerification.validateWithinCooldown())
                .doesNotThrowAnyException();
    }

    @Test
    void 이메일_인증_쿨다운시간이_지나지_않으면_예외가_발생한다() {
        LocalDateTime createdAt = LocalDateTime.now();
        EmailVerification emailVerification = new EmailVerification(new Email("email@domain.com"), VerificationType.REGISTER);
        setCreatedAt(emailVerification, createdAt);

        assertThatThrownBy(() -> emailVerification.validateWithinCooldown())
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.EMAIL_COOL_TIME.getMessage());
    }

    @Test
    void 인증번호_검증에서_인증번호가_다르면_예외가_발생한다() {
        EmailVerification emailVerification = new EmailVerification(new Email("email@domain.com"), VerificationType.REGISTER);
        setCode(emailVerification, "1234");

        assertThatThrownBy(() -> emailVerification.verify("5678"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.WRONG_EMAIL_CODE.getMessage());
    }

    @Test
    void 인증번호_검증에서_인증번호의_유효기간이_지나면_예외가_발생한다() {
        EmailVerification emailVerification = new EmailVerification(new Email("email@domain.com"), VerificationType.REGISTER);
        setCode(emailVerification, "1234");
        setCreatedAt(emailVerification, LocalDateTime.now().minusMinutes(10));

        assertThatThrownBy(() -> emailVerification.verify("1234"))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.EXPIRED_EMAIL_CODE.getMessage());
    }

    private void setCreatedAt(Object entity, LocalDateTime time) {
        try {
            Field field = entity.getClass().getSuperclass().getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(entity, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setCode(Object entity, String code) {
        try {
            Field field = entity.getClass().getDeclaredField("code");
            field.setAccessible(true);
            field.set(entity, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}