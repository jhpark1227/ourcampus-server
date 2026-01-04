package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FacilityRepository extends JpaRepository<Facility, Long>, FacilityRepositoryCustom {

    @Query("""
                    SELECT f
                    FROM Facility f
                    LEFT JOIN FETCH f.building
                    WHERE f.name LIKE concat('%', :keyword, '%') AND f.university=:university
            """)
    List<Facility> findByNameLikeAndUniversity(String keyword, University university);

    List<Facility> findByBuilding(Building building);

    List<Facility> findByUniversityAndCategory(University university, FacilityCategory category);
}
