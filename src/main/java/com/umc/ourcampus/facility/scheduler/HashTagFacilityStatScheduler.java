package com.umc.ourcampus.facility.scheduler;

import com.umc.ourcampus.facility.application.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HashTagFacilityStatScheduler {

    private final FacilityService facilityService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("해시태그별 인기 시설 통계 초기화 시작");
        facilityService.refreshHashTagFacilityStats();
        log.info("해시태그별 인기 시설 통계 초기화 완료");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refresh() {
        log.info("해시태그별 인기 시설 통계 갱신 시작");
        facilityService.refreshHashTagFacilityStats();
        log.info("해시태그별 인기 시설 통계 갱신 완료");
    }
}
