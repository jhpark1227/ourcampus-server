package com.example.school.auth.presentation;

import com.example.school.auth.application.AuthCommandService;
import com.example.school.auth.application.AuthQueryService;
import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.auth.application.dto.request.LoginRequest;
import com.example.school.auth.application.dto.response.LoginResponse;
import com.example.school.global.apiPayload.ApiResponse;
import com.example.school.global.validation.annotation.CheckKeyword;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
        LoginResponse response = authQueryService.login(user);
        return ResponseEntity.ok(response);
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

    @GetMapping("/search-schools")
    public ApiResponse<List<AuthResponseDTO.SchoolResDTO>> searchSchools(
            @RequestParam("query") @CheckKeyword String keyword) {

        List<AuthResponseDTO.SchoolResDTO> resList = authQueryService.searchSchool(keyword);

        return ApiResponse.onSuccess(resList);
    }
}
