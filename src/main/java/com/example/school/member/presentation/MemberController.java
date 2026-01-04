package com.example.school.member.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.member.application.MemberService;
import com.example.school.member.application.dto.request.RegisterRequest;
import com.example.school.member.application.dto.request.VerificationEmailRequest;
import com.example.school.member.application.dto.response.MemberInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/members/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        memberService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberInfoResponse response = memberService.getUserInfo(principal.memberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody @Valid VerificationEmailRequest request) {
        memberService.sendVerificationEmail(request);
        return ResponseEntity.ok().build();
    }
}

