package com.umc.ourcampus.member.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.member.application.AdminService;
import com.umc.ourcampus.member.application.dto.request.AdminRegisterRequest;
import com.umc.ourcampus.member.application.dto.response.AdminInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin/register")
    public void register(@RequestBody AdminRegisterRequest request) {
        adminService.register(request);
    }

    @GetMapping("/admin/me")
    public AdminInfoResponse getMyInfo(@AuthenticationPrincipal UserPrincipal principal) {
        return adminService.getMyInfo(principal.memberId());
    }
}
