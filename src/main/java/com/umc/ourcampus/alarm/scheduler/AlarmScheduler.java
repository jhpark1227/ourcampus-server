package com.umc.ourcampus.alarm.scheduler;

import com.umc.ourcampus.alarm.application.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmScheduler {

    private final AlarmService alarmService;

    @Scheduled(cron = "0 0 4 * * *")
    public void purgeExpiredAlarms() {
        log.info("만료 알림 삭제 시작");
        int deleted = alarmService.purgeExpiredAlarms();
        log.info("만료 알림 삭제 완료 (삭제 건수: {})", deleted);
    }
}