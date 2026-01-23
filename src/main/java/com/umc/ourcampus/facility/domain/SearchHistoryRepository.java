package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.facility.application.dto.response.SearchKeywordResponse;
import com.umc.ourcampus.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>, SearchHistoryRepositoryCustom {

    @Query("""
                   SELECT new com.umc.ourcampus.facility.domain.SearchKeyword(sh.keyword.value)
                   FROM SearchHistory sh
                   WHERE sh.id IN (
                       SELECT MAX(sh2.id)
                       FROM SearchHistory sh2
                       WHERE sh2.member = :member
                       GROUP BY sh2.keyword.value
                   )
                   ORDER BY sh.createdAt DESC
            """)
    List<SearchKeywordResponse> findKeywordByMemberOrderByLastest(Member member, Pageable pageable);

    void deleteByMemberAndKeyword(Member member, SearchKeyword keyword);
}
