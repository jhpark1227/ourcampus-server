package com.example.school.user.presentation;

import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.validation.annotation.ExistFacility;
import com.example.school.global.validation.annotation.ExistMember;
import com.example.school.global.validation.annotation.ExistReview;
import com.example.school.review.domain.Review;
import com.example.school.user.application.UserCommandService;
import com.example.school.user.application.UserQueryService;
import com.example.school.user.converter.UserConverter;
import com.example.school.user.domain.Inquiry;
import com.example.school.user.domain.Member;
import com.example.school.user.dto.UserRequestDTO;
import com.example.school.user.dto.UserResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    //리뷰 작성
    @PostMapping(value = "/review", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "리뷰 작성 API", description = "리뷰를 작성하는 API")
    public ApiResponse<UserResponseDTO.CreateReviewResultDTO> createReview(
            @RequestPart(value = "image", required = false) List<MultipartFile> imgFile,
            @RequestPart @Valid UserRequestDTO.ReviewDTO request,
            @ExistFacility @RequestParam(name = "facilityId") Long facilityId,
            @ExistMember @RequestParam(name = "memberId") Long memberId) {

        Review review = userCommandService.createReview(imgFile, memberId, facilityId, request);
        return ApiResponse.onSuccess(UserConverter.toCreateReviewResultDTO(review));
    }

    @PutMapping(value = "/review/modify", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 리뷰 수정 API", description = "나의 리뷰를 수정하는 API이며, reviewId, facilityId, memberId가 모두 일치할 시 수정가능합니다")
    public ApiResponse<UserResponseDTO.UpdateReviewResultDTO> modifyReview(
            @ExistReview @RequestParam(name = "reviewId") Long reviewId,
            @RequestPart(value = "image", required = false) List<MultipartFile> imgFile,
            @RequestPart(name = "review", required = false) String updateReviewDTOString,
            @ExistFacility @RequestParam(name = "facilityId") Long facilityId,
            @ExistMember @RequestParam(name = "memberId") Long memberId) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDTO.UpdateReviewDTO updateReviewDTO = null;
        if (updateReviewDTOString != null) {
            updateReviewDTO = objectMapper.readValue(updateReviewDTOString, UserRequestDTO.UpdateReviewDTO.class);
        }

        Review updatedReview = userCommandService.updateReview(memberId, facilityId, reviewId, updateReviewDTO,
                imgFile);
        return ApiResponse.onSuccess(UserConverter.toUpdateReviewResultDTO(updatedReview));
    }

    //프로필 정보 수정
    @PutMapping(value = "/update-profile", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<UserResponseDTO.UpdateProfileResultDTO> updateProfile(
            @ExistMember @RequestParam(name = "memberId") Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile profileImage,
            @RequestPart(name = "updateProfileReqDTO", required = false) String updateProfileReqDTOString)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDTO.UpdateProfileDTO updateProfileReqDTO = null;
        if (updateProfileReqDTOString != null) {
            updateProfileReqDTO = objectMapper.readValue(updateProfileReqDTOString,
                    UserRequestDTO.UpdateProfileDTO.class);
        }

        Member updatedMember = userCommandService.updateProfile(memberId, updateProfileReqDTO, profileImage);
        return ApiResponse.onSuccess(UserConverter.toUpdateProfileResultDTO(updatedMember));
    }

    //시설별 리뷰 조회
    /*
    @GetMapping("/details/{facilityId}/review")
    public ApiResponse<UserResponseDTO.ReviewPreViewListDTO> facilityReview(@PathVariable(name="facilityId") Long facilityId,
                                                                            @RequestParam(name="page") Integer page){
        Page<Review> reviewList = userQueryService.findByFacility(facilityId, page);
        return ApiResponse.onSuccess(UserConverter.reviewPreViewListDTO(reviewList));
    }*/

    @GetMapping("/{facilityId}/reviews/byFacility")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "시설별 리뷰 조회 API", description = "시설별로 리뷰목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    public ApiResponse<Page<UserResponseDTO.ReviewPreViewDTO>> facilityReview(
            @PathVariable(name = "facilityId") Long facilityId,
            @RequestParam(name = "page") Integer page) {
        Page<UserResponseDTO.ReviewPreViewDTO> reviewList = userQueryService.findByFacility(facilityId, page);
        return ApiResponse.onSuccess(reviewList);
    }

    //나의 리뷰 조회
    @GetMapping("/{memberId}/reviews/byMember")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 리뷰 목록 조회 API", description = "나의 리뷰들의 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Page<UserResponseDTO.ReviewPreViewDTO>> getReviewList(
            @ExistMember @PathVariable(name = "memberId") Long memberId, @RequestParam(name = "page") Integer page) {
        Page<UserResponseDTO.ReviewPreViewDTO> reviewList = userQueryService.getReviewList(memberId, page);
        return ApiResponse.onSuccess(reviewList);
    }

    //모든 리뷰 조회
    @GetMapping("/allReviews")
    @Operation(summary = "모든 리뷰 목록 조회 API", description = "모든 리뷰들의 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Page<UserResponseDTO.ReviewPreViewDTO>> getAllReviewList(
            @RequestParam(name = "page") Integer page) {
        Page<UserResponseDTO.ReviewPreViewDTO> reviewList = userQueryService.getAllReviewList(page);
        return ApiResponse.onSuccess(reviewList);
    }

    //리뷰삭제
    @DeleteMapping("/review/delete")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 리뷰 삭제 API", description = "나의 리뷰를 삭제하는 API이며, reviewId, facilityId, memberId가 모두 일치할 시 삭제 가능합니다")
    public ApiResponse<String> deleteReview(
            @ExistReview @RequestParam(name = "reviewId") Long reviewId,
            @ExistFacility @RequestParam(name = "facilityId") Long facilityId,
            @ExistMember @RequestParam(name = "memberId") Long memberId) {

        userCommandService.deleteReview(memberId, facilityId, reviewId);
        return ApiResponse.onSuccess("Review deleted successfully");
    }

    //문의하기
    @PostMapping("/inquiry")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "문의하기 API", description = "문의하는 API")
    public ApiResponse<UserResponseDTO.CreateInquiryResultDTO> createInquiry(
            @RequestBody @Valid UserRequestDTO.InquiryDTO request,
            @ExistMember @RequestParam(name = "memberId") Long memberId) {
        Inquiry inquiry = userCommandService.createInquiry(memberId, request);
        return ApiResponse.onSuccess(UserConverter.toCreateInquiryResultDTO(inquiry));
    }


    @GetMapping("/info")
    public ApiResponse<UserResponseDTO.Info> getInfo(Authentication auth) {
        Member member = (Member) auth.getPrincipal();

        UserResponseDTO.Info res = userQueryService.getInfo(member.getId());

        return ApiResponse.onSuccess(res);
    }
}

