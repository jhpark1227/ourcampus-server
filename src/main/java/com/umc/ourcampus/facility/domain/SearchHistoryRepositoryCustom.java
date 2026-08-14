package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.university.domain.University;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchHistoryRepositoryCustom {
    List<SearchKeyword> findPopularKeywordByUniversity(University university, LocalDateTime from, int size);
}
