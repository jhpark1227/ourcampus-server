package com.umc.ourcampus.facility.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityThemeRepository extends JpaRepository<FacilityTheme, Long> {
    List<FacilityTheme> findByTheme(Theme theme);

    void deleteByFacility(Facility facility);
}
