package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.UsageStatusService;
import com.example.school.facility.application.dto.response.UsageStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsageStatusController {

    private final UsageStatusService usageStatusService;

    @GetMapping("/facilities/usage-status")
    public Page<UsageStatusResponse> getUsageStatus(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("size") int size
    ) {
        return usageStatusService.getUsageStatus(memberPrincipal.universityId(), PageRequest.ofSize(size));
    }
}
