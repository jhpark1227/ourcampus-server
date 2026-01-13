package com.example.school.review.domain;

import static com.example.school.review.domain.QReview.review;

import com.example.school.facility.domain.ReservableFacility;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewStarRatingCount> getStarRatingCounts(ReservableFacility facility) {
        return queryFactory
                .select(Projections.constructor(ReviewStarRatingCount.class, review.starRating, review.count()))
                .from(review)
                .where(review.reservation.facility.eq(facility))
                .groupBy(review.starRating)
                .fetch();
    }
}
