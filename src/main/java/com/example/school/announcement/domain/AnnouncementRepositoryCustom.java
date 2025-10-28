package com.example.school.announcement.domain;

import com.example.school.facility.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnnouncementRepositoryCustom {
    Page<Announcement> findByType(School school, AnnouncementType type, Pageable pageable);
}
