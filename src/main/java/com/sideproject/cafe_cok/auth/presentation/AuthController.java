package com.sideproject.cafe_cok.auth.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.application.OAuthClient;
import com.sideproject.cafe_cok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.cafe_cok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.cafe_cok.auth.dto.response.AccessTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final OAuthClient oAuthClient;

    @GetMapping("/login")
    @Operation(summary = "소셜 로그인 기능")
    public ResponseEntity<AccessAndRefreshTokenResponse> login(@RequestParam("code") final String code) {

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "access Token 갱신")
    public ResponseEntity<AccessTokenResponse> refresh(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {

        AccessTokenResponse response = authService.generateAccessToken(tokenRenewalRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal LoginMember loginMember) {

        authService.logout(loginMember);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<Void> withdrawal(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestParam("reason") final String reason) {

        authService.withdrawal(loginMember, reason);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
