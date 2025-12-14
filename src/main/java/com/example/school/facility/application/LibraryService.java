package com.example.school.facility.application;

import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.RestTemplateRes;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final MemberRepository memberRepository;

    //@Value("${flask-server}")
    //private String address;

    public FacilityResponseDTO.LibraryStatus getLibraryStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!member.getUniversity().getName().equals("울산대학교")) {
            throw new GeneralException(ErrorStatus.NO_CONTENT);
        }

        String uri = "/api/v1/library/울산대학교";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RestTemplateRes.Common<RestTemplateRes.LibraryStatus>> res =
                restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        if (!res.getBody().getIsSuccess()) {
            throw new GeneralException(ErrorStatus.NO_CONTENT);
        }

        RestTemplateRes.LibraryStatus status = res.getBody().getResult();

        List<FacilityResponseDTO.LibraryDetail> list = status.getList().stream().map(detail -> {
            return FacilityResponseDTO.LibraryDetail.builder()
                    .name(detail.getName())
                    .total(detail.getTotal())
                    .current(detail.getTotal() - detail.getCurrent())
                    .status(detail.getStatus()).build();
        }).collect(Collectors.toList());

        return new FacilityResponseDTO.LibraryStatus(list);
    }
}
