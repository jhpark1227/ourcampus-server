package com.example.school.facility.domain;

import com.example.school.facility.application.dto.ScoreDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f from Facility f left join fetch f.building where f.name like concat('%', :keyword, '%')and f.university=:university")
    Page<Facility> findByNameLikeAndUniversity(@Param("keyword") String keyword,
                                               @Param("university") com.example.school.university.domain.University university,
                                               Pageable page);


    @Query("select new com.example.school.facility.application.dto.ScoreDTO(f,avg(r.score)) " +
            "from Facility f join Review r on r.facility = f " +
            "group by f.id")
    List<ScoreDTO> findAllWithReview();

    List<Facility> findByBuilding(Building building);

    List<Facility> findByCategory(FacilityCategory category);
}
