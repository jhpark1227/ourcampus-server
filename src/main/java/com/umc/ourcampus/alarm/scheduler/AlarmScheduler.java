package com.umc.ourcampus.alarm.scheduler;

import com.umc.ourcampus.alarm.application.AlarmService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmScheduler {

    private static final int RETENTION_DAYS = 7;
    private static final int BATCH_SIZE = 1000;

    private final AlarmService alarmService;

    @Scheduled(cron = "0 0 4 * * *")
    public void purgeExpiredAlarms() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(RETENTION_DAYS);
        log.info("만료 알림 삭제 시작 (기준 발송 시각: {})", threshold);

        int totalDeleted = 0;
        int deleted;
        do {
            deleted = alarmService.purgeAlarmsScheduledBefore(threshold, BATCH_SIZE);
            totalDeleted += deleted;
        } while (deleted == BATCH_SIZE);

        log.info("만료 알림 삭제 완료 (삭제 건수: {})", totalDeleted);
    }
}