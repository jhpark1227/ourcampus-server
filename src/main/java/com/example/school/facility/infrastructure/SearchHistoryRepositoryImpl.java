package com.example.school.facility.infrastructure;

import static com.example.school.facility.domain.QSearchHistory.searchHistory;

import com.example.school.facility.domain.SearchHistoryRepositoryCustom;
import com.example.school.facility.domain.SearchKeyword;
import com.example.school.university.domain.University;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchHistoryRepositoryImpl implements SearchHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchKeyword> findPopularKeywordByUniversity(University university, int size) {
        return queryFactory.from(searchHistory)
                .where(searchHistory.member.university.eq(university))
                .groupBy(searchHistory.keyword)
                .select(Projections.constructor(SearchKeyword.class, searchHistory.keyword.value))
                .orderBy(searchHistory.keyword.count().desc())
                .limit(size)
                .fetch();
    }
}
