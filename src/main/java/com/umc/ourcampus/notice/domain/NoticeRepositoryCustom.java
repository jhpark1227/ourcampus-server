package com.umc.ourcampus.notice.domain;

import com.umc.ourcampus.university.domain.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NoticeRepositoryCustom {
    Page<Notice> findByUniversityAndType(University university, NoticeType type, Pageable pageable);
}
