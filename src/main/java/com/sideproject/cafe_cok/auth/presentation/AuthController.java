package com.sideproject.cafe_cok.auth.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.application.OAuthClient;
import com.sideproject.cafe_cok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.cafe_cok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.cafe_cok.auth.dto.response.AccessTokenResponse;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final OAuthClient oAuthClient;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping("/login")
    @Operation(summary = "소셜 로그인 기능")
    public ResponseEntity<AccessAndRefreshTokenResponse> login(@RequestParam("code") final String code) {

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/login");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @Operation(summary = "access Token 갱신")
    public ResponseEntity<AccessTokenResponse> refresh(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {

        AccessTokenResponse response = authService.generateAccessToken(tokenRenewalRequest);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/refresh");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal LoginMember loginMember) {

        authService.logout(loginMember);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/logout");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<Void> withdrawal(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestParam("reason") final String reason) {

        authService.withdrawal(loginMember, reason);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/withdrawal");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
