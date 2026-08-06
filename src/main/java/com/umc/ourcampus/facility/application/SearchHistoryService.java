package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.facility.application.dto.response.SearchKeywordResponse;
import com.umc.ourcampus.facility.domain.PopularKeywordStat;
import com.umc.ourcampus.facility.domain.PopularKeywordStatRepository;
import com.umc.ourcampus.facility.domain.SearchHistoryRepository;
import com.umc.ourcampus.facility.domain.SearchKeyword;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchHistoryService {

    private static final int RETENTION_DAYS = 30;
    private static final int POPULAR_PERIOD_DAYS = 7;
    private static final int POPULAR_KEYWORD_SIZE = 20;

    private final SearchHistoryRepository searchHistoryRepository;
    private final PopularKeywordStatRepository popularKeywordStatRepository;
    private final UniversityRepository universityRepository;

    @Transactional(readOnly = true)
    public List<SearchKeywordResponse> findPopularByUniversityId(long universityId, int size) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));

        return popularKeywordStatRepository
                .findByUniversityOrderByRankAsc(university, PageRequest.of(0, size))
                .stream()
                .map(stat -> SearchKeywordResponse.from(stat.getKeyword()))
                .toList();
    }

    public void refreshPopularKeywordStats() {
        LocalDateTime from = LocalDateTime.now().minusDays(POPULAR_PERIOD_DAYS);

        for (University university : universityRepository.findAll()) {
            popularKeywordStatRepository.deleteAllByUniversity(university);

            List<SearchKeyword> keywords = searchHistoryRepository
                    .findPopularKeywordByUniversity(university, from, POPULAR_KEYWORD_SIZE);

            List<PopularKeywordStat> stats = new ArrayList<>();
            for (int rank = 0; rank < keywords.size(); rank++) {
                stats.add(PopularKeywordStat.of(university, keywords.get(rank), rank + 1));
            }
            popularKeywordStatRepository.saveAll(stats);
        }
    }

    public int purgeExpiredSearchHistories() {
        return searchHistoryRepository.deleteCreatedBefore(LocalDateTime.now().minusDays(RETENTION_DAYS));
    }
}