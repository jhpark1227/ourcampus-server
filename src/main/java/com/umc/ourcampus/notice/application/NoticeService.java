package com.umc.ourcampus.notice.application;

import com.umc.ourcampus.notice.domain.NoticeType;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.notice.application.dto.response.NoticeDetailResponse;
import com.umc.ourcampus.notice.application.dto.response.NoticeResponse;
import com.umc.ourcampus.notice.domain.Notice;
import com.umc.ourcampus.notice.domain.NoticeRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UniversityRepository universityRepository;

    public NoticeDetailResponse findNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND));
        return NoticeDetailResponse.from(notice);
    }

    public Page<NoticeResponse> findNotices(Long universityId, NoticeType type, Pageable pageable) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Page<Notice> notices = noticeRepository.findByUniversityAndType(university, type, pageable);

        return notices.map(NoticeResponse::from);
    }
}
