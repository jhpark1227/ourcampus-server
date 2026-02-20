package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @Test
    void 비밀번호를_생성한다() {
        String passwordValue = "testPassword123";

        Assertions.assertThatCode(() -> new Password(passwordValue))
                .doesNotThrowAnyException();
    }

    @Test
    void 비밀번호에_숫자가_포함되지_않으면_예외가_발생한다() {
        String passwordValue = "testPassword";

        Assertions.assertThatThrownBy(() -> new Password(passwordValue))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.PASSWORD_FORMAT_ERROR.getMessage());
    }

    @Test
    void 비밀번호에_알파벳이_포함되지_않으면_예외가_발생한다() {
        String passwordValue = "12345678";

        Assertions.assertThatThrownBy(() -> new Password(passwordValue))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.PASSWORD_FORMAT_ERROR.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"pw1234", "testPassword1234"})
    void 비밀번호의_길이가_길거나_짧으면_예외가_발생한다(String passwordValue) {
        Assertions.assertThatThrownBy(() -> new Password(passwordValue))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.PASSWORD_FORMAT_ERROR.getMessage());
    }
}