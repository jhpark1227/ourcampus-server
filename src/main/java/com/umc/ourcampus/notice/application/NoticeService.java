package com.umc.ourcampus.notice.application;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.notice.application.dto.request.CreateNoticeRequest;
import com.umc.ourcampus.notice.application.dto.request.UpdateNoticeRequest;
import com.umc.ourcampus.notice.application.dto.response.NoticeResponse;
import com.umc.ourcampus.notice.domain.Notice;
import com.umc.ourcampus.notice.domain.NoticeRepository;
import com.umc.ourcampus.notice.domain.NoticeType;
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

    public NoticeResponse findNoticeById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND));
        return NoticeResponse.from(notice);
    }

    public Page<NoticeResponse> findNotices(Long universityId, NoticeType type, Pageable pageable) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Page<Notice> notices = noticeRepository.findByUniversityAndType(university, type, pageable);

        return notices.map(NoticeResponse::from);
    }

    public void createNotice(UserPrincipal principal, long universityId, CreateNoticeRequest request) {
        if (principal.universityId() != universityId) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Notice notice = new Notice(request.title(), request.content(), request.type(), university);
        noticeRepository.save(notice);
    }

    public void deleteNotice(UserPrincipal principal, long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND));
        if (!notice.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        noticeRepository.delete(notice);
    }

    public void updateNotice(UserPrincipal principal, long noticeId, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND));
        if (!notice.getUniversity().equalId(principal.universityId())) {
            throw new ApplicationException(ErrorStatus.PERMISSION_ERROR);
        }
        notice.update(request.title(), request.content(), request.type());
    }
}
