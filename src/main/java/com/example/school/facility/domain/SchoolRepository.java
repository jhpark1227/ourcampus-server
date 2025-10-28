package com.example.school.facility.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findByName(String name);
}
