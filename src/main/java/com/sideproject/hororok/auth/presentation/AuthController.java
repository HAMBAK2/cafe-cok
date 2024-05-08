package com.sideproject.hororok.auth.presentation;


import com.sideproject.hororok.auth.application.AuthService;
import com.sideproject.hororok.auth.application.OAuthClient;
import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.dto.OAuthMember;
import com.sideproject.hororok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.hororok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.hororok.auth.dto.response.AccessTokenResponse;
import com.sideproject.hororok.global.error.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
@Tag(name = "Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final OAuthClient oAuthClient;

    @GetMapping("/login")
    @Operation(summary = "소셜 로그인 기능")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = AccessAndRefreshTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "OAuth 서버와 통신 문제 또는 알 수 없는 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "인증받은 유저의 정보를 찾을 수 없는 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<AccessAndRefreshTokenResponse> login(
            @Parameter(name = "code", description = "소셜 로그인 요청으로 발급받은 code")
            @RequestParam("code") final String code) {

        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        AccessAndRefreshTokenResponse response = authService.generateAccessAndRefreshToken(oAuthMember);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    @Operation(summary = "access Token 갱신")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "access Token 갱신 성공",
                    content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "알 수 없는 서버 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "refresh 토큰이 유효하지 않은 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "요청 refresh Token과 저장된 token 값이 다른 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    public ResponseEntity<AccessTokenResponse> refresh(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {

        AccessTokenResponse response = authService.generateAccessToken(tokenRenewalRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공")})
    public ResponseEntity<Void> logout(@AuthenticationPrincipal LoginMember loginMember) {

        authService.logout(loginMember);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원탈퇴")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공")})
    public ResponseEntity<Void> withdrawal(@AuthenticationPrincipal LoginMember loginMember) {

        authService.withdrawal(loginMember);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
