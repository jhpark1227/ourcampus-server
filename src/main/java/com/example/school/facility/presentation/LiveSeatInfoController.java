package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.LiveSeatInfoService;
import com.example.school.facility.application.dto.response.LiveSeatInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LiveSeatInfoController {

    private final LiveSeatInfoService liveSeatInfoService;

    @GetMapping("/facilities/live-seat-info")
    public Page<LiveSeatInfoResponse> getLiveSeatInfo(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("size") int size
    ) {
        return liveSeatInfoService.getLiveSeatInfo(memberPrincipal.universityId(), PageRequest.ofSize(size));
    }
}
