package com.umc.ourcampus.alarm.application;

import com.umc.ourcampus.alarm.application.dto.response.AlarmResponse;
import com.umc.ourcampus.alarm.application.dto.response.UnreadAlarmResponse;
import com.umc.ourcampus.alarm.domain.Alarm;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.domain.AlarmRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public List<AlarmResponse> getMyAlarms(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Alarm> alarms = alarmRepository.findSendAlarmByMember((member));
        List<AlarmResponse> responses = alarms.stream()
                .map(AlarmResponse::from)
                .toList();
        alarms.forEach(Alarm::read);
        return responses;
    }

    public UnreadAlarmResponse getMyUnreadAlarmCount(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        long unreadCount = alarmRepository.findUnreadCount(member);

        return new UnreadAlarmResponse(unreadCount);
    }
}
