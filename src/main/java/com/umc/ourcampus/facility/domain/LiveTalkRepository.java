package com.umc.ourcampus.facility.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveTalkRepository extends JpaRepository<LiveTalk, Long> {
    Page<LiveTalk> findByFacilityOrderByCreatedAtDesc(Facility facility, Pageable pageable);
}
