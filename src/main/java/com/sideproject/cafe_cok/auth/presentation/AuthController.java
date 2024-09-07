package com.sideproject.cafe_cok.auth.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.application.OAuthClient;
import com.sideproject.cafe_cok.auth.dto.request.TokenRenewalRequest;
import com.sideproject.cafe_cok.auth.dto.response.AccessAndRefreshTokenResponse;
import com.sideproject.cafe_cok.auth.dto.response.AccessTokenResponse;
import com.sideproject.cafe_cok.auth.dto.response.AuthEmptyResponse;
import com.sideproject.cafe_cok.cafe.presentation.CafeController;
import com.sideproject.cafe_cok.member.presentation.MemberController;
import com.sideproject.cafe_cok.plan.presentation.PlanController;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        response.add(linkTo(methodOn(AuthController.class).login(code)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinates(null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinatesAndKeyword(null, null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(MemberController.class).detail(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(PlanController.class).save(null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/login");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @Operation(summary = "access Token 갱신")
    public ResponseEntity<AccessTokenResponse> refresh(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {

        AccessTokenResponse response = authService.generateAccessToken(tokenRenewalRequest);
        response.add(linkTo(methodOn(AuthController.class).refresh(tokenRenewalRequest)).withSelfRel().withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/refresh");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<AuthEmptyResponse> logout(@AuthenticationPrincipal LoginMember loginMember) {
        AuthEmptyResponse response = authService.logout(loginMember);
        response.add(linkTo(methodOn(AuthController.class).logout(loginMember)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(AuthController.class).login(null)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinates(null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinatesAndKeyword(null, null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(MemberController.class).detail(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(PlanController.class).save(null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/logout");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<AuthEmptyResponse> withdrawal(@AuthenticationPrincipal LoginMember loginMember,
                                                        @RequestParam("reason") final String reason) {

        AuthEmptyResponse response = authService.withdrawal(loginMember, reason);
        response.add(linkTo(methodOn(AuthController.class).withdrawal(loginMember, reason)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(AuthController.class).login(null)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinates(null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(CafeController.class).findByCoordinatesAndKeyword(null, null, null, null)).withRel("search").withType("GET"))
                .add(linkTo(methodOn(MemberController.class).detail(null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(PlanController.class).save(null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("auth/withdrawal");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
