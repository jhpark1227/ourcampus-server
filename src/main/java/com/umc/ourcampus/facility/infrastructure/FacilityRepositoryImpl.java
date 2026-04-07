package com.umc.ourcampus.facility.infrastructure;

import static com.umc.ourcampus.facility.domain.QFacility.facility;
import static com.umc.ourcampus.review.domain.QHashTag.hashTag;
import static com.umc.ourcampus.review.domain.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.FacilityRepositoryCustom;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Facility> findTopFacilitiesByHashTag(HashTag targetHashTag, int limit, University university) {
        return queryFactory.select(facility)
                .from(review)
                .join(review.reservation.facility, facility)
                .join(review.hashTags, hashTag)
                .where(hashTag.eq(targetHashTag), facility.university.eq(university))
                .groupBy(facility.id)
                .orderBy(review.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Facility> findByUniversityAndCategory(University university, FacilityCategory category) {
        return queryFactory.selectFrom(facility)
                .where(
                        university == null ? null : facility.university.eq(university),
                        category == null ? null : facility.category.eq(category)
                )
                .fetch();
    }
}
