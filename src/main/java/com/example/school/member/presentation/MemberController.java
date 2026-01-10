package com.example.school.member.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.member.application.MemberService;
import com.example.school.member.application.dto.request.EmailFindRequest;
import com.example.school.member.application.dto.request.PasswordChangeRequest;
import com.example.school.member.application.dto.request.PasswordResetRequest;
import com.example.school.member.application.dto.request.ProfileImageChangeRequest;
import com.example.school.member.application.dto.request.RegisterRequest;
import com.example.school.member.application.dto.request.WithDrawRequest;
import com.example.school.member.application.dto.response.EmailFindResponse;
import com.example.school.member.application.dto.response.MemberInfoResponse;
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
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody WithDrawRequest request
    ) {
        memberService.withdraw(request, memberPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberInfoResponse response = memberService.getUserInfo(principal.memberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members/find-email")
    public EmailFindResponse findEmail(@RequestBody EmailFindRequest request) {
        return memberService.findEmail(request);
    }

    @PutMapping("/members/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody PasswordChangeRequest request
    ) {
        memberService.changePassword(request, memberPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/members/profile-image")
    public ResponseEntity<Void> changeProfileImage(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody ProfileImageChangeRequest request
    ) {
        memberService.changeProfileImage(request, memberPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }
}

