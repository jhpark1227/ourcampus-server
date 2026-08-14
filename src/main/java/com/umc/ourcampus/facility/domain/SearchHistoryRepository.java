package com.umc.ourcampus.facility.domain;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>, SearchHistoryRepositoryCustom {

    @Modifying
    @Query(value = """
                DELETE FROM search_history
                WHERE created_at < :threshold
            """, nativeQuery = true)
    int deleteCreatedBefore(LocalDateTime threshold);
}
