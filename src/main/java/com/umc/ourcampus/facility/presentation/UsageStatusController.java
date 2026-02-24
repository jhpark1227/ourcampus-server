package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.UsageStatusService;
import com.umc.ourcampus.facility.application.dto.response.UsageStatusResponse;
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
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("size") int size
    ) {
        return usageStatusService.getUsageStatus(userPrincipal.universityId(), PageRequest.ofSize(size));
    }
}
