package com.example.school.notice.domain;

import com.example.school.university.domain.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NoticeRepositoryCustom {
    Page<Notice> findByUniversityAndType(University university, NoticeType type, Pageable pageable);
}
