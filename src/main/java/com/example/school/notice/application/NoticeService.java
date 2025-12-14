package com.example.school.notice.application;

import com.example.school.facility.application.dto.RestTemplateRes;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.notice.application.dto.response.NoticeDetailResponse;
import com.example.school.notice.application.dto.response.NoticeResponse;
import com.example.school.notice.domain.Notice;
import com.example.school.notice.domain.NoticeRepository;
import com.example.school.notice.domain.NoticeType;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UniversityRepository universityRepository;

    //@Value("${flask-server}")
    private String address;

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

    @Scheduled(cron = "0 50 23 * * *")
    public void getAnnouncement() {
        String uri = address + "/api/v1/announcement/울산대학교";
        University university = universityRepository.findByName("울산대학교");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestTemplateRes.Common<RestTemplateRes.AnnouncementList>> res =
                restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        RestTemplateRes.AnnouncementList list = res.getBody().getResult();
        list.getList().forEach(object -> {
            Boolean isPresent = noticeRepository.findByTitle(object.getTitle()).isPresent();
            if (isPresent) {
                return;
            }
            noticeRepository.save(Notice.builder()
                    .title(object.getTitle())
                    .content(object.getContent())
                    .type(NoticeType.valueOf(object.getType()))
                    .university(university)
                    .build()
            );
        });
    }
}
