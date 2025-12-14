package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByUniversity(University university);
}
