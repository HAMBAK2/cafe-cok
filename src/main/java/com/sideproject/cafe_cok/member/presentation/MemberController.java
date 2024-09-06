package com.sideproject.cafe_cok.member.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.combination.presentation.CombinationController;
import com.sideproject.cafe_cok.member.application.MemberService;
import com.sideproject.cafe_cok.member.dto.request.MemberFeedbackRequest;
import com.sideproject.cafe_cok.member.dto.response.MemberResponse;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.member.dto.response.MemberPlansResponse;
import com.sideproject.cafe_cok.plan.presentation.PlanController;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "members", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping
    @Operation(summary = "회원 조회")
    public ResponseEntity<MemberResponse> detail(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse response = memberService.find(loginMember);
        response.add(linkTo(methodOn(MemberController.class).detail(loginMember)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(MemberController.class).update(null, null, null)).withRel("update").withType("PATCH"))
                .add(linkTo(methodOn(MemberController.class).saveFeedback(null, null)).withRel("save-feedback").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("members/detail");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 수정")
    public ResponseEntity<MemberResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                                 @RequestParam("nickname") String nickname) {

        MemberResponse response = memberService.update(loginMember, nickname, file);
        response.add(linkTo(methodOn(MemberController.class).update(loginMember, file, nickname)).withSelfRel().withType("PATCH"))
                .add(linkTo(methodOn(MemberController.class).detail(null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("members/update");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/feedbacks")
    @Operation(summary = "회원 개선의견 저장")
    public ResponseEntity<MemberResponse> saveFeedback(@AuthenticationPrincipal LoginMember loginMember,
                                                       @RequestBody MemberFeedbackRequest request) {

        MemberResponse response = memberService.saveFeedback(loginMember, request);
        response.add(linkTo(methodOn(MemberController.class).saveFeedback(loginMember, request)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(MemberController.class).detail(null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("members/saveFeedback");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/plans")
    @Operation(summary = "회원의 모든 계획 조회")
    public ResponseEntity<MemberPlansResponse> findPlanList(@AuthenticationPrincipal LoginMember loginMember,
                                                            @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
                                                            @RequestParam(defaultValue = "SAVED") PlanStatus status) {

        MemberPlansResponse response = memberService.findPlanList(loginMember, sortBy, status);
        response.add(linkTo(methodOn(MemberController.class).findPlanList(loginMember, sortBy, status)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(PlanController.class).detail(null, null)).withRel("plan-detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("members/findPlanList");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
