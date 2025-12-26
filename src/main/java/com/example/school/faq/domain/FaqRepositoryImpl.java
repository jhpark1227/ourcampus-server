package com.example.school.faq.domain;

import static com.example.school.faq.domain.QFaq.faq;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Faq> findByType(FaqType type, Pageable pageable) {
        return queryFactory.selectFrom(faq)
                .where(typeEq(type))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression typeEq(FaqType type) {
        return type != null ? faq.type.eq(type) : null;
    }
}
