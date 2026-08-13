package com.umc.ourcampus.alarm.presentation;

import com.umc.ourcampus.alarm.application.AlarmService;
import com.umc.ourcampus.alarm.application.dto.response.AlarmSliceResponse;
import com.umc.ourcampus.alarm.application.dto.response.UnreadAlarmResponse;
import com.umc.ourcampus.auth.domain.UserPrincipal;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private static final int MAX_PAGE_SIZE = 50;

    private final AlarmService alarmService;

    @GetMapping("/me/alarms")
    public AlarmSliceResponse getMyAlarms(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "cursorScheduledTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorScheduledTime,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(MAX_PAGE_SIZE) int size
    ) {
        return alarmService.getMyAlarms(userPrincipal.memberId(), cursorScheduledTime, cursorId, size);
    }

    @GetMapping("/me/alarms/unread-count")
    public UnreadAlarmResponse getMyUnreadAlarmCount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return alarmService.getMyUnreadAlarmCount(userPrincipal.memberId());
    }

    @PatchMapping("/me/alarms/read-all")
    public ResponseEntity<Void> readAllAlarms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        alarmService.readAllAlarms(userPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }
}