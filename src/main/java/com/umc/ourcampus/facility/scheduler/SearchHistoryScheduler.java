package com.umc.ourcampus.facility.scheduler;

import com.umc.ourcampus.facility.application.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchHistoryScheduler {

    private final SearchHistoryService searchHistoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("인기 검색어 통계 초기화 시작");
        searchHistoryService.refreshPopularKeywordStats();
        log.info("인기 검색어 통계 초기화 완료");
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshPopularKeywordStats() {
        log.info("인기 검색어 통계 갱신 시작");
        searchHistoryService.refreshPopularKeywordStats();
        log.info("인기 검색어 통계 갱신 완료");
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void purgeExpiredSearchHistories() {
        log.info("만료 검색 기록 삭제 시작");
        int deleted = searchHistoryService.purgeExpiredSearchHistories();
        log.info("만료 검색 기록 삭제 완료 (삭제 건수: {})", deleted);
    }
}