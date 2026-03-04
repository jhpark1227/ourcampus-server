package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @Test
    void 주소가_이메일_형식이면_인스턴스를_생성한다() {
        String address = "testEmail@domain.com";

        Assertions.assertThatCode(() -> new Email(address))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"email@", "email", "@domain.com"})
    void 주소가_이메일_형식이_아니면_예외가_발생한다(String address) {
        Assertions.assertThatThrownBy(() -> new Email(address))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.EMAIL_FORMAT_ERROR.getMessage());
    }
}
