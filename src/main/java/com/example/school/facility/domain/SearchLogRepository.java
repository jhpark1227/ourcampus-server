package com.example.school.facility.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    Optional<SearchLog> findByValueAndSchool(String value, School school);

    List<SearchLog> findTop5BySchoolOrderByCountDesc(School school);
}
