package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HeadCountTest {

    @Test
    void 예약인원이_1명_미만이면_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> new HeadCount(0))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.HEAD_COUNT_INVALID_RANGE.getMessage());
    }

    @Test
    void 예약인원이_1000명_초과면_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> new HeadCount(1001))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.HEAD_COUNT_INVALID_RANGE.getMessage());
    }
}