package com.umc.ourcampus.review.infrastructure;

import static com.umc.ourcampus.facility.domain.QFacility.facility;
import static com.umc.ourcampus.review.domain.QReview.review;
import static com.umc.ourcampus.review.domain.QHashTag.hashTag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.review.domain.HashTagRepositoryCustom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HashTagRepositoryImpl implements HashTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HashTag> findTopHashTags(int size) {
        return queryFactory.select(hashTag)
                .from(review)
                .join(review.hashTags, hashTag)
                .join(review.reservation.facility, facility)
                .groupBy(hashTag.id)
                .orderBy(facility.count().desc())
                .limit(size)
                .fetch();
    }
}
