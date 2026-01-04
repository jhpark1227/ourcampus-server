package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import java.util.List;

public interface SearchHistoryRepositoryCustom {
    List<SearchKeyword> findPopularKeywordByUniversity(University university, int size);
}
