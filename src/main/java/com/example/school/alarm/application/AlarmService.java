package com.example.school.alarm.application;

import com.example.school.alarm.application.dto.response.AlarmResponse;
import com.example.school.alarm.application.dto.response.UnreadAlarmResponse;
import com.example.school.alarm.domain.Alarm;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.domain.AlarmRepository;
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
