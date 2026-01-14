package com.example.school.facility.domain;

import com.example.school.university.domain.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveSeatInfoRepository extends JpaRepository<LiveSeatInfo, Long> {
    Page<LiveSeatInfo> findByFacility_University(University facilityUniversity, Pageable pageable);
}
