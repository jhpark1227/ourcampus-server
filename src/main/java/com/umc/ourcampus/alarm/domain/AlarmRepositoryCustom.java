package com.umc.ourcampus.alarm.domain;

import com.umc.ourcampus.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepositoryCustom {

    List<Alarm> findSentAlarms(
            Member member,
            LocalDateTime now,
            LocalDateTime cursorScheduledTime,
            Long cursorId,
            int limit
    );
}