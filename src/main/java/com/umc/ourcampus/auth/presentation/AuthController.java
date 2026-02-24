package com.umc.ourcampus.auth.presentation;

import com.umc.ourcampus.auth.application.AuthService;
import com.umc.ourcampus.auth.application.dto.request.AccessTokenReissueRequest;
import com.umc.ourcampus.auth.application.dto.request.AdminLoginRequest;
import com.umc.ourcampus.auth.application.dto.request.LoginRequest;
import com.umc.ourcampus.auth.application.dto.request.LogoutRequest;
import com.umc.ourcampus.auth.application.dto.response.AccessTokenReissueResponse;
import com.umc.ourcampus.auth.application.dto.response.AdminLoginResponse;
import com.umc.ourcampus.auth.application.dto.response.LoginResponse;
import com.umc.ourcampus.auth.domain.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/auth/login")
    public AdminLoginResponse adminLogin(@RequestBody @Valid AdminLoginRequest request) {
        return authService.adminLogin(request);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AccessTokenReissueResponse> refreshAccessToken(@RequestBody AccessTokenReissueRequest request) {
        AccessTokenReissueResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request, userPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }
}
