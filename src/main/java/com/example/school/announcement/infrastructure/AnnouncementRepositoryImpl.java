package com.example.school.announcement.infrastructure;

import static com.example.school.announcement.domain.QAnnouncement.announcement;

import com.example.school.announcement.domain.Announcement;
import com.example.school.announcement.domain.AnnouncementType;
import com.example.school.announcement.domain.AnnouncementRepositoryCustom;
import com.example.school.facility.domain.School;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class AnnouncementRepositoryImpl implements AnnouncementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Announcement> findByType(School school, AnnouncementType type, Pageable pageable) {
        List<Announcement> list = queryFactory
                .selectFrom(announcement)
                .where(
                        typeEq(type),
                        schoolEq(school)
                )
                .orderBy(announcement.createdAt.desc())
                .fetch();

        long totalCount = queryFactory
                .select(announcement.count())
                .from(announcement)
                .where(
                        typeEq(type),
                        schoolEq(school)
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }

    BooleanExpression typeEq(AnnouncementType type) {
        return type != null ? announcement.type.eq(type) : null;
    }

    BooleanExpression schoolEq(School school) {
        return school != null ? announcement.school.eq(school) : null;
    }
}
