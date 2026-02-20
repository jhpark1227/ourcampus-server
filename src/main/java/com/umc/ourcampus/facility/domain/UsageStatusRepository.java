package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.university.domain.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsageStatusRepository extends JpaRepository<UsageStatus, Long> {
    @Query("""
                SELECT usageStatus
                FROM UsageStatus usageStatus
                WHERE usageStatus.university = :university
                AND usageStatus.createdAt = (
                    SELECT MAX(usageStatus.createdAt)
                    FROM UsageStatus usageStatus
                    WHERE usageStatus.university = :university
                )
                ORDER BY usageStatus.occupiedSeats
            """)
    Page<UsageStatus> findLastestByUniversity(University university, Pageable pageable);
}
