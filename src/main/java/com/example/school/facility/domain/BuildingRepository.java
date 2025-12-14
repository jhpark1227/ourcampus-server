package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("select b from Building b where b.university=:university")
    List<Building> findBySchoolAndInCategoryWithFacility(@Param("university") University university);

    List<Building> findAllByUniversity(University university);
}
