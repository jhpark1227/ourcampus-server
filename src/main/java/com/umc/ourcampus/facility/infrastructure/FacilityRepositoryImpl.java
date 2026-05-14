package com.umc.ourcampus.facility.infrastructure;

import static com.umc.ourcampus.facility.domain.QFacility.facility;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.FacilityRepositoryCustom;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Facility> findTopFacilitiesByHashTag(HashTag targetHashTag, int limit, University university) {
        String sql = """
                SELECT f.*
                FROM hash_tag_review hr
                  JOIN review FORCE ON review.id = hr.review_id AND review.deleted_at IS NULL
                  JOIN reservation ON reservation.id = review.reservation_id AND reservation.deleted_at IS NULL
                  JOIN facility f ON f.id = reservation.facility_id AND f.deleted_at IS NULL
                WHERE hr.hash_tag_id = :hashTagId
                GROUP BY f.id
                ORDER BY COUNT(*) DESC
                LIMIT 5;
                """;
        return entityManager.createNativeQuery(sql, Facility.class)
                .setParameter("hashTagId", targetHashTag.getId())
                .getResultList();
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
