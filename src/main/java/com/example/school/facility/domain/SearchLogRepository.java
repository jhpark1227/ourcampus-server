package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    Optional<SearchLog> findByValueAndUniversity(String value, University university);

    List<SearchLog> findTop5ByUniversityOrderByCountDesc(University university);
}
