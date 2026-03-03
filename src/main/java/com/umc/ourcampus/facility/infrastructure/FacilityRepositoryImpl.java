package com.umc.ourcampus.facility.infrastructure;

import static com.umc.ourcampus.facility.domain.QFacility.facility;
import static com.umc.ourcampus.review.domain.QHashTag.hashTag;
import static com.umc.ourcampus.review.domain.QReview.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityAndHashTag;
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
    public List<FacilityAndHashTag> findFacilityAndHashTagIdByHashTags(List<HashTag> hashTags) {
        return queryFactory.from(review)
                .join(review.reservation.facility)
                .join(review.hashTags, hashTag)
                .where(hashTag.in(hashTags))
                .select(Projections.constructor(FacilityAndHashTag.class, facility, hashTag))
                .distinct()
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
