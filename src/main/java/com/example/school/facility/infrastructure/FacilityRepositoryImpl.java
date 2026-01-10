package com.example.school.facility.infrastructure;

import static com.example.school.facility.domain.QFacility.facility;
import static com.example.school.review.domain.QHashTag.hashTag;
import static com.example.school.review.domain.QReview.review;

import com.example.school.facility.domain.FacilityAndHashTag;
import com.example.school.facility.domain.FacilityRepositoryCustom;
import com.example.school.review.domain.HashTag;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .join(review.reservation.facility, facility)
                .join(review.hashTags, hashTag)
                .where(hashTag.in(hashTags))
                .select(Projections.constructor(FacilityAndHashTag.class, facility, hashTag))
                .distinct()
                .fetch();
    }
}
