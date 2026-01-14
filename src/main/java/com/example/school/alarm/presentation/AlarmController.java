package com.example.school.alarm.presentation;

import com.example.school.alarm.application.AlarmService;
import com.example.school.alarm.application.dto.response.AlarmResponse;
import com.example.school.alarm.application.dto.response.UnreadAlarmResponse;
import com.example.school.auth.domain.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/me/alarms")
    public List<AlarmResponse> getMyAlarms(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return alarmService.getMyAlarms(memberPrincipal.memberId());
    }

    @GetMapping("/me/alarms/unread-count")
    public UnreadAlarmResponse getMyUnreadAlarmCount(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return alarmService.getMyUnreadAlarmCount(memberPrincipal.memberId());
    }
}
