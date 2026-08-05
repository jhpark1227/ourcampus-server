package com.umc.ourcampus.alarm.domain;

import static com.umc.ourcampus.alarm.domain.QAlarm.alarm;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findSentAlarms(
            Member member,
            LocalDateTime now,
            LocalDateTime cursorScheduledTime,
            Long cursorId,
            int limit
    ) {
        return queryFactory.selectFrom(alarm)
                .where(
                        alarm.member.eq(member),
                        alarm.scheduledTime.loe(now),
                        cursorLt(cursorScheduledTime, cursorId)
                )
                .orderBy(alarm.scheduledTime.desc(), alarm.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression cursorLt(LocalDateTime cursorScheduledTime, Long cursorId) {
        if (cursorScheduledTime == null || cursorId == null) {
            return null;
        }
        return alarm.scheduledTime.lt(cursorScheduledTime)
                .or(alarm.scheduledTime.eq(cursorScheduledTime).and(alarm.id.lt(cursorId)));
    }
}