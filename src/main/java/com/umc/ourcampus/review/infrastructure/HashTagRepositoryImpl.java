package com.umc.ourcampus.review.infrastructure;

import static com.umc.ourcampus.review.domain.QHashTag.hashTag;

import com.querydsl.core.types.dsl.Expressions;
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
    public List<HashTag> findRandomHashTags(int size, long seed) {
        return queryFactory.selectFrom(hashTag)
                .orderBy(Expressions.numberTemplate(Double.class, "RAND({0})", seed).asc())
                .limit(size)
                .fetch();
    }
}
