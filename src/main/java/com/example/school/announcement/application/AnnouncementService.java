package com.example.school.announcement.application;

import com.example.school.announcement.application.dto.AnnouncementRes;
import com.example.school.announcement.domain.Announcement;
import com.example.school.announcement.domain.AnnouncementType;
import com.example.school.announcement.domain.AnnouncementRepository;
import com.example.school.facility.domain.School;
import com.example.school.facility.application.dto.RestTemplateRes;
import com.example.school.facility.domain.SchoolRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;


    //@Value("${flask-server}")
    private String address;

    public AnnouncementRes.Samples getSamples(Long memberId) {
        Member member = userRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        School school = member.getSchool();

        List<Announcement> entities = announcementRepository.findBySchoolOrderByCreatedAtDesc(
                school,
                PageRequest.of(0, 4)
        );

        List<AnnouncementRes.Sample> list =
                entities.stream().map(entity -> new AnnouncementRes.Sample(entity.getId(), entity.getTitle()))
                        .collect(Collectors.toList());

        return new AnnouncementRes.Samples(list, list.size());
    }

    public AnnouncementRes.Detail getDetail(Long id) {
        Announcement entity = announcementRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANNOUNCE_NOT_FOUND));

        return new AnnouncementRes.Detail(entity);
    }

    public AnnouncementRes.ListDto getList(Long id, String type, Integer page) {
        Member member = userRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        AnnouncementType announcementType = null;
        if (StringUtils.hasText(type)) {
            announcementType = AnnouncementType.valueOf(type);
        }

        Page<Announcement> entities = announcementRepository.findByType(member.getSchool(), announcementType,
                PageRequest.of(page - 1, 15));

        return new AnnouncementRes.ListDto(entities);
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void getAnnouncement() {
        String uri = address + "/api/v1/announcement/울산대학교";
        School school = schoolRepository.findByName("울산대학교");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestTemplateRes.Common<RestTemplateRes.AnnouncementList>> res =
                restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        RestTemplateRes.AnnouncementList list = res.getBody().getResult();
        list.getList().forEach(object -> {
            Boolean isPresent = announcementRepository.findByTitle(object.getTitle()).isPresent();
            if (isPresent) {
                return;
            }
            announcementRepository.save(Announcement.builder()
                    .title(object.getTitle())
                    .content(object.getContent())
                    .type(AnnouncementType.valueOf(object.getType()))
                    .school(school)
                    .build()
            );
        });
    }

}
