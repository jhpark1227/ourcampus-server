package com.example.school.review.infrastructure;

import static com.example.school.facility.domain.QFacility.facility;
import static com.example.school.review.domain.QHashTag.hashTag;
import static com.example.school.review.domain.QReview.review;

import com.example.school.review.domain.HashTag;
import com.example.school.review.domain.HashTagRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .join(review.facility, facility)
                .groupBy(hashTag.id)
                .orderBy(facility.count().desc())
                .limit(size)
                .fetch();
    }
}
