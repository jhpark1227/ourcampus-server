package com.umc.ourcampus.alarm.application;

import com.umc.ourcampus.alarm.application.dto.response.AlarmResponse;
import com.umc.ourcampus.alarm.application.dto.response.AlarmSliceResponse;
import com.umc.ourcampus.alarm.application.dto.response.UnreadAlarmResponse;
import com.umc.ourcampus.alarm.domain.Alarm;
import com.umc.ourcampus.alarm.domain.AlarmRepository;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int RETENTION_DAYS = 7;

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AlarmSliceResponse getMyAlarms(
            long memberId,
            LocalDateTime cursorScheduledTime,
            Long cursorId,
            int size
    ) {
        Member member = findMember(memberId);
        int pageSize = Math.min(size, MAX_PAGE_SIZE);

        List<Alarm> alarms = alarmRepository.findSentAlarms(
                member,
                LocalDateTime.now(),
                cursorScheduledTime,
                cursorId,
                pageSize + 1
        );

        boolean hasNext = alarms.size() > pageSize;
        List<AlarmResponse> responses = alarms.stream()
                .limit(pageSize)
                .map(AlarmResponse::from)
                .toList();

        return AlarmSliceResponse.of(responses, hasNext);
    }

    @Transactional(readOnly = true)
    public UnreadAlarmResponse getMyUnreadAlarmCount(long memberId) {
        Member member = findMember(memberId);
        long unreadCount = alarmRepository.findUnreadCount(member);

        return new UnreadAlarmResponse(unreadCount);
    }

    public void readAllAlarms(long memberId) {
        Member member = findMember(memberId);
        alarmRepository.markAllAsRead(member);
    }

    public int purgeExpiredAlarms() {
        return alarmRepository.deleteScheduledBefore(LocalDateTime.now().minusDays(RETENTION_DAYS));
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
    }
}