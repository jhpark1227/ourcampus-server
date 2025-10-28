package com.example.school.auth.presentation;

import com.example.school.auth.application.AuthCommandService;
import com.example.school.auth.application.AuthQueryService;
import com.example.school.auth.application.MailService;
import com.example.school.auth.application.dto.AuthRequestDTO;
import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.validation.annotation.CheckKeyword;
import com.example.school.user.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthCommandService authCommandService;

    private final AuthQueryService authQueryService;
    private final MailService mailService;

    // 회원가입
    @PostMapping(value = "/register")
    public ApiResponse<Member> register(@RequestPart(value = "image", required = false) MultipartFile profileImage,
                                        @RequestPart AuthRequestDTO.RegisterReqDTO registerReqDTO) {
        if (authQueryService.validateDuplicateUserId(registerReqDTO.getUserId())) {
            return ApiResponse.onFailure(ErrorStatus.USERID_DUPLICATE.getCode(),
                    ErrorStatus.USERID_DUPLICATE.getMessage());
        }

        if (authQueryService.checkUserIdFormat(registerReqDTO.getUserId())) {
            return ApiResponse.onFailure(ErrorStatus.USER_FORMAT_ERROR.getCode(),
                    ErrorStatus.USER_FORMAT_ERROR.getMessage());
        }

        if (!authQueryService.checkPassword((registerReqDTO.getPassword()))) {
            return ApiResponse.onFailure(ErrorStatus.PASSWORD_FORMAT_ERROR.getCode(),
                    ErrorStatus.PASSWORD_FORMAT_ERROR.getMessage());
        }

        if (!authQueryService.checkIdentifyNumFormat(registerReqDTO.getIdentify_num())) {
            return ApiResponse.onFailure(ErrorStatus.IDENTIFYNUM_FORMAT_ERROR.getCode(),
                    ErrorStatus.IDENTIFYNUM_FORMAT_ERROR.getMessage());
        }

        if (authQueryService.validateDuplicateEmail(registerReqDTO.getEmail())) {
            return ApiResponse.onFailure(ErrorStatus.EMAIL_DUPLICATE.getCode(),
                    ErrorStatus.EMAIL_DUPLICATE.getMessage());
        }

        if (!authQueryService.checkEmailFormat(registerReqDTO.getEmail())) {
            return ApiResponse.onFailure(ErrorStatus.EMAIL_FORMAT_ERROR.getCode(),
                    ErrorStatus.EMAIL_FORMAT_ERROR.getMessage());
        }

        if (!authQueryService.checkNicknameDuplicate(registerReqDTO.getNickname())) {
            return ApiResponse.onFailure(ErrorStatus.NICKNAME_DUPLICATE.getCode(),
                    ErrorStatus.NICKNAME_DUPLICATE.getMessage());
        }

        if (!authQueryService.checkSchoolFormat(registerReqDTO.getSchool().getId(),
                registerReqDTO.getSchool().getName())) {
            return ApiResponse.onFailure(ErrorStatus.SCHOOL_FORMAT_ERROR.getCode(),
                    ErrorStatus.SCHOOL_FORMAT_ERROR.getMessage());
        }

        return ApiResponse.onSuccess(authQueryService.register(registerReqDTO, profileImage));
    }

    // 로그인
    @PostMapping(value = "/login")
    public ApiResponse<AuthResponseDTO.LoginResDTO> login(@RequestBody AuthRequestDTO.LoginReqDTO user) {
        return ApiResponse.onSuccess(authQueryService.login(user));
    }

    //로그아웃
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String accessToken) {
        authCommandService.logout(accessToken);
        return ApiResponse.onSuccess("로그아웃 처리 되었습니다.");
    }

    //회원탈퇴
    @DeleteMapping(value = "/delete")
    public ApiResponse<String> withdrawUser(@RequestHeader("Authorization") String accessToken) {
        authCommandService.withdrawUser(accessToken);
        return ApiResponse.onSuccess("회원탈퇴 처리 되었습니다.");
    }

    // 이메일 전송
    @PostMapping(value = "/email-send")
    public ApiResponse<String> sendEmail(@RequestBody AuthRequestDTO.EmailAuthReqDTO emailAuthReqDTO) {

        if (!authQueryService.checkEmailFormat(emailAuthReqDTO.getEmail())) {
            return ApiResponse.onFailure(ErrorStatus.EMAIL_FORMAT_ERROR.getCode(),
                    ErrorStatus.EMAIL_FORMAT_ERROR.getMessage());
        }

//        if (authQueryService.validateDuplicateEmail(emailAuthReqDTO.getEmail())) {
//            return ApiResponse.onFailure(ErrorStatus.EMAIL_DUPLICATE.getCode(), ErrorStatus.EMAIL_DUPLICATE.getMessage());
//        }
        String verificationCode = mailService.sendCertificationMail(emailAuthReqDTO.getEmail());

        return ApiResponse.onSuccess(verificationCode);
    }

    // 이메일 인증
    @PostMapping(value = "/email-auth")
    public ApiResponse<String> authEmail(@RequestBody AuthRequestDTO.EmailAuthReqDTO emailAuthReqDTO) {
        Boolean isAuthenticationSuccessful = mailService.verifyCertificationCode(emailAuthReqDTO.getEmail(),
                emailAuthReqDTO.getAuthCode());

        if (isAuthenticationSuccessful) {
            // 인증이 성공한 경우
            return ApiResponse.onSuccess("이메일 인증 성공!");
        } else {
            // 인증이 실패한 경우
            return ApiResponse.onFailure(ErrorStatus.EMAIL_CODE_ERROR.getCode(),
                    ErrorStatus.EMAIL_CODE_ERROR.getMessage());
        }
    }

    // 비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/change-password")
    public ApiResponse<String> changePassword(@RequestBody AuthRequestDTO.ChangePasswordReqDTO changePasswordReqDTO) {

        // 비밀번호 변경 수행
        authQueryService.changePassword(changePasswordReqDTO);

        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 아이디 찾기
    @PostMapping(value = "/find-userId")
    public ApiResponse<AuthResponseDTO.findUserIdDTO> findUsername(
            @RequestBody AuthRequestDTO.EmailAuthReqDTO emailAuthReqDTO) {
        if (!authQueryService.checkEmailFormat(emailAuthReqDTO.getEmail())) {
            return ApiResponse.onFailure(ErrorStatus.EMAIL_FORMAT_ERROR.getCode(),
                    ErrorStatus.EMAIL_FORMAT_ERROR.getMessage());
        }

        // 인증 번호가 일치하는지 확인
        Boolean isAuthenticationSuccessful = mailService.verifyCertificationCode(emailAuthReqDTO.getEmail(),
                emailAuthReqDTO.getAuthCode());

        if (isAuthenticationSuccessful) {
            // 인증이 성공한 경우, 아이디 및 생성일자 반환
            Member foundMember = authQueryService.findMemberByEmail(emailAuthReqDTO.getEmail());
            AuthResponseDTO.findUserIdDTO responseDTO = new AuthResponseDTO.findUserIdDTO(foundMember.getUsername(),
                    foundMember.getCreatedAt());
            return ApiResponse.onSuccess(responseDTO);
        } else {
            // 인증이 실패한 경우
            return ApiResponse.onFailure(ErrorStatus.EMAIL_CODE_ERROR.getCode(),
                    ErrorStatus.EMAIL_CODE_ERROR.getMessage());
        }

    }

    @PostMapping(value = "/find-password")
    public ApiResponse<String> findPassword(@RequestBody AuthRequestDTO.FindPwRequest findPwRequest) {
        try {
            // 서비스를 호출하여 비밀번호 찾기 기능 수행
            Boolean isPasswordFound = authQueryService.findPasswd(findPwRequest);

            if (isPasswordFound) {
                // 비밀번호 찾기가 성공한 경우
                return ApiResponse.onSuccess("비밀번호 찾기 인증 성공");
            } else {
                // 비밀번호 찾기 실패 (오류에 따라 다른 응답을 보낼 수 있음)
                return ApiResponse.onFailure(ErrorStatus.FIND_PASSWORD_ERROR.getCode(),
                        ErrorStatus.FIND_PASSWORD_ERROR.getMessage());
            }
        } catch (GeneralException e) {
            // GeneralException이 발생한 경우에 대한 처리
            return ApiResponse.onFailure(e.getErrorStatus().getCode(), e.getErrorStatus().getMessage());
        } catch (Exception e) {
            // 기타 예외가 발생한 경우에 대한 처리
            e.printStackTrace();
            return ApiResponse.onFailure(ErrorStatus.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorStatus.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @PostMapping("/reissue")
    public ApiResponse<AuthResponseDTO.ReissueRespDto> reissue(@RequestHeader("Authorization") String refreshToken) {
        AuthResponseDTO.ReissueRespDto reissueRespDto = authQueryService.reissue(refreshToken);
        return ApiResponse.onSuccess(reissueRespDto);
    }

    @GetMapping("/search-schools")
    public ApiResponse<List<AuthResponseDTO.SchoolResDTO>> searchSchools(
            @RequestParam("query") @CheckKeyword String keyword) {

        List<AuthResponseDTO.SchoolResDTO> resList = authQueryService.searchSchool(keyword);

        return ApiResponse.onSuccess(resList);
    }
}
