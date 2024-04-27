package com.sideproject.hororok.member.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.member.application.MyPageService;
import com.sideproject.hororok.member.dto.response.*;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/myPage")
@Tag(name = "MyPage", description = "마이페이지 관련 API")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 상단의 사용자 프로필을 나타냄")
    public ResponseEntity<MyPageProfileResponse> profile(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageProfileResponse response = myPageService.profile(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag/save")
    @Operation(summary = "마이페이지의 저장 태그를 눌렀을 때 동작")
    public ResponseEntity<MyPageTagSaveResponse> tagSave(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageTagSaveResponse response = myPageService.tagSave(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved/plan")
    @Operation(summary = "계획 탭의 저장된 계획을 나타내는 API")
    public ResponseEntity<MyPagePlanResponse> savedPlan(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        MyPagePlanResponse response = myPageService.savedPlan(loginMember, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared/plan")
    @Operation(summary = "계획 탭의 공유된 계획을 나타내는 API")
    public ResponseEntity<MyPagePlanResponse> sharedPlan(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        MyPagePlanResponse response = myPageService.sharedPlan(loginMember, sortBy);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved/plans")
    @Operation(summary = "저장된 계획의 전체 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansResponse> savedPlans(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        MyPagePlansResponse response = myPageService.savedPlans(loginMember, sortBy, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared/plans")
    @Operation(summary = "공유된 계획의 전체 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansResponse> sharedPlans(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        MyPagePlansResponse response = myPageService.sharedPlans(loginMember, sortBy, page, size);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/plan/{planId}")
    @Operation(summary = "마이페이지 계획탭에서 하나의 계획(여정)을 선택했을 때 동작")
    public ResponseEntity<MyPagePlanDetailResponse> planDetail(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long planId){

        MyPagePlanDetailResponse response = myPageService.planDetail(planId);
        return ResponseEntity.ok(response);
    }
}