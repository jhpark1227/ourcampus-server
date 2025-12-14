package com.example.school.notice.infrastructure;

import static com.example.school.notice.domain.QNotice.notice;

import com.example.school.notice.domain.Notice;
import com.example.school.notice.domain.NoticeRepositoryCustom;
import com.example.school.notice.domain.NoticeType;
import com.example.school.university.domain.University;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> findByUniversityAndType(University university, NoticeType type, Pageable pageable) {
        List<Notice> list = queryFactory
                .selectFrom(notice)
                .where(
                        typeEq(type),
                        universityEq(university)
                )
                .orderBy(notice.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        typeEq(type),
                        universityEq(university)
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }

    BooleanExpression typeEq(NoticeType type) {
        return type != null ? notice.type.eq(type) : null;
    }

    BooleanExpression universityEq(University university) {
        return university != null ? notice.university.eq(university) : null;
    }
}
