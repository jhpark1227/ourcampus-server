package com.example.school.member.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.member.application.InquiryService;
import com.example.school.member.application.dto.request.InquireRequest;
import com.example.school.member.application.dto.response.InquiryResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/inquiries")
    public ResponseEntity<Void> inquire(
            @RequestBody @Valid InquireRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        long inquiryId = inquiryService.inquire(request, memberPrincipal.memberId());
        return ResponseEntity.created(URI.create("/inquiries/" + inquiryId))
                .build();
    }

    @GetMapping("/me/inquiries")
    public List<InquiryResponse> getMyInquiries(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return inquiryService.getMyInquiries(memberPrincipal.memberId());
    }
}
