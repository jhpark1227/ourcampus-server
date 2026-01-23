package com.umc.ourcampus.review.domain;

import static com.umc.ourcampus.review.domain.QReview.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.facility.domain.Facility;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewStarRatingCount> getStarRatingCounts(Facility facility) {
        return queryFactory
                .select(Projections.constructor(ReviewStarRatingCount.class, review.starRating, review.count()))
                .from(review)
                .where(review.reservation.facility.eq(facility))
                .groupBy(review.starRating)
                .fetch();
    }
}
