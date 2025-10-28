package com.example.school.announcement.domain;

import com.example.school.facility.domain.School;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, AnnouncementRepositoryCustom {
    List<Announcement> findBySchoolOrderByCreatedAtDesc(School school, PageRequest page);

    Optional<Announcement> findByTitle(String title);
}
