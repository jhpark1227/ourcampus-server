package com.example.school.facility.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("select b from Building b left join fetch b.facilities where b.school=:school and b.inCategory = true")
    List<Building> findBySchoolAndInCategoryWithFacility(@Param("school") School school);

    List<Building> findAllBySchool(School school);

    @Query("select b from Building b left join fetch b.buildingHours where b.id=:id")
    Optional<Building> findByIdWithBuildingHours(@Param("id") Long buildingId);
}
