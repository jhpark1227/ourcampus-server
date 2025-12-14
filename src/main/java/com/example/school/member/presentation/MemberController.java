package com.example.school.member.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.validation.annotation.ExistMember;
import com.example.school.member.application.MemberCommandService;
import com.example.school.member.application.MemberQueryService;
import com.example.school.member.application.dto.MemberRequestDTO;
import com.example.school.member.application.dto.MemberResponseDTO;
import com.example.school.member.application.dto.request.RegisterRequest;
import com.example.school.member.application.dto.response.MemberInfoResponse;
import com.example.school.member.converter.UserConverter;
import com.example.school.member.domain.Inquiry;
import com.example.school.member.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @PostMapping(value = "/members/register")
    public ResponseEntity<Void> register(
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart RegisterRequest registerRequest
    ) throws InterruptedException {
        memberCommandService.register(registerRequest, profileImage);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/members/update-profile", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<MemberResponseDTO.UpdateProfileResultDTO> updateProfile(
            @ExistMember @RequestParam(name = "memberId") Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "updateProfileReqDTO", required = false) String updateProfileReqDTOString)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        MemberRequestDTO.UpdateProfileDTO updateProfileReqDTO = null;
        if (updateProfileReqDTOString != null) {
            updateProfileReqDTO = objectMapper.readValue(updateProfileReqDTOString,
                    MemberRequestDTO.UpdateProfileDTO.class);
        }

        Member updatedMember = memberCommandService.updateProfile(memberId, updateProfileReqDTO, profileImage);
        return ApiResponse.onSuccess(UserConverter.toUpdateProfileResultDTO(updatedMember));
    }

    @PostMapping("/members/inquiry")
    @Operation(summary = "문의하기 API", description = "문의하는 API")
    public ApiResponse<MemberResponseDTO.CreateInquiryResultDTO> createInquiry(
            @RequestBody @Valid MemberRequestDTO.InquiryDTO request,
            @ExistMember @RequestParam(name = "memberId") Long memberId) {
        Inquiry inquiry = memberCommandService.createInquiry(memberId, request);
        return ApiResponse.onSuccess(UserConverter.toCreateInquiryResultDTO(inquiry));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getMyInfo(@AuthenticationPrincipal MemberPrincipal principal) {
        MemberInfoResponse response = memberQueryService.getUserInfo(principal.memberId());
        return ResponseEntity.ok(response);
    }
}

