package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.university.domain.University;
import java.util.List;

public interface SearchHistoryRepositoryCustom {
    List<SearchKeyword> findPopularKeywordByUniversity(University university, int size);
}
