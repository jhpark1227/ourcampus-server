package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.university.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByUniversity(University university);
}
