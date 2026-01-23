package com.umc.ourcampus.notice.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    Optional<Notice> findByTitle(String title);
}
