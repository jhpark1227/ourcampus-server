package com.example.school.facility.domain;

import com.example.school.facility.application.dto.ScoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findBySchoolAndTagIsNotNull(School school);

    @Query("select f from Facility f left join fetch f.building left join fetch f.facilityHours where f.id=:id")
    Optional<Facility> findByIdWithDetail(@Param("id") Long id);

    List<Facility> findByKeywordAndSchool(FacilityKeyword keyword, School school);

    @Query("select f from Facility f left join fetch f.building where f.name like concat('%', :keyword, '%')and f.school=:school")
    Page<Facility> findByNameLikeAndSchool(@Param("keyword") String keyword, @Param("school") School school,
                                           Pageable page);

    List<Facility> findBySchoolAndIsThemeIsTrue(School school);

    @Query("select new com.example.school.facility.dto.ScoreDTO(f,avg(r.score)) " +
            "from Facility f join Review r on r.facility = f " +
            "group by f.id")
    List<ScoreDTO> findAllWithReview();
}
