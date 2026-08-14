package com.umc.ourcampus.facility.infrastructure;

import static com.umc.ourcampus.facility.domain.QSearchHistory.searchHistory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.facility.domain.SearchHistoryRepositoryCustom;
import com.umc.ourcampus.facility.domain.SearchKeyword;
import com.umc.ourcampus.university.domain.University;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchHistoryRepositoryImpl implements SearchHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchKeyword> findPopularKeywordByUniversity(University university, LocalDateTime from, int size) {
        return queryFactory.from(searchHistory)
                .where(
                        searchHistory.university.eq(university),
                        searchHistory.createdAt.goe(from)
                )
                .groupBy(searchHistory.keyword)
                .select(Projections.constructor(SearchKeyword.class, searchHistory.keyword.value))
                .orderBy(searchHistory.keyword.count().desc())
                .limit(size)
                .fetch();
    }
}