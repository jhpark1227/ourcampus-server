package com.umc.ourcampus.member.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.member.application.MemberService;
import com.umc.ourcampus.member.application.dto.request.EmailFindRequest;
import com.umc.ourcampus.member.application.dto.request.PasswordChangeRequest;
import com.umc.ourcampus.member.application.dto.request.PasswordResetRequest;
import com.umc.ourcampus.member.application.dto.request.ProfileImageChangeRequest;
import com.umc.ourcampus.member.application.dto.request.RegisterRequest;
import com.umc.ourcampus.member.application.dto.request.WithDrawRequest;
import com.umc.ourcampus.member.application.dto.response.EmailFindResponse;
import com.umc.ourcampus.member.application.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        memberService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/withdraw")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody WithDrawRequest request
    ) {
        memberService.withdraw(request, userPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal UserPrincipal principal) {
        MemberInfoResponse response = memberService.getUserInfo(principal.memberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members/find-email")
    public EmailFindResponse findEmail(@RequestBody EmailFindRequest request) {
        return memberService.findEmail(request);
    }

    @PutMapping("/members/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PasswordChangeRequest request
    ) {
        memberService.changePassword(request, userPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/members/profile-image")
    public ResponseEntity<Void> changeProfileImage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ProfileImageChangeRequest request
    ) {
        memberService.changeProfileImage(request, userPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }
}

