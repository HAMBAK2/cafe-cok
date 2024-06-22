package com.sideproject.cafe_cok.member.presentation;


import com.sideproject.cafe_cok.member.dto.request.MemberFeedbackRequest;
import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.member.application.MyPageService;
import com.sideproject.cafe_cok.member.dto.response.*;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.review.application.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/myPage")
@Tag(name = "MyPage", description = "마이페이지 관련 API")
public class MyPageController {

    private final MyPageService myPageService;
    private final ReviewService reviewService;

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 상단의 사용자 프로필을 나타냄")
    public ResponseEntity<MyPageProfileResponse> profile(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageProfileResponse response = myPageService.profile(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/profile/edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 수정")
    public ResponseEntity<MyPageProfileEditResponse> editProfile(@AuthenticationPrincipal LoginMember loginMember,
                                                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                                                 @RequestParam("nickname") String nickname) throws IOException {

        MyPageProfileEditResponse response = myPageService.editProfile(loginMember, nickname, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved/plans")
    @Operation(summary = "저장된 계획의 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansResponse> savedPlans(@AuthenticationPrincipal LoginMember loginMember,
                                                          @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
                                                          @RequestParam(defaultValue = "1") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer size) {

        MyPagePlansResponse response = myPageService.getPlans(loginMember, sortBy, PlanStatus.SAVED, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared/plans")
    @Operation(summary = "공유된 계획의 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansResponse> sharedPlans(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
                                                           @RequestParam(defaultValue = "1") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer size) {

        MyPagePlansResponse response = myPageService.getPlans(loginMember, sortBy, PlanStatus.SHARED, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/plan/{planId}")
    @Operation(summary = "마이페이지 계획탭에서 하나의 계획(여정)을 선택했을 때 동작")
    public ResponseEntity<MyPagePlanDetailResponse> planDetail(@AuthenticationPrincipal LoginMember loginMember,
                                                               @PathVariable Long planId){

        MyPagePlanDetailResponse response = myPageService.planDetail(loginMember, planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reviews")
    @Operation(summary = "마이페이지 리뷰탭을 눌렀을 때 동작")
    public ResponseEntity<MyPageReviewResponse> reviews(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageReviewResponse response = reviewService.getReviews(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/combination")
    @Operation(summary = "마이 페이지 내 조합을 눌렀을 때 동작")
    public ResponseEntity<MyPageCombinationResponse> combination(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageCombinationResponse response = myPageService.combination(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved/plans/all")
    @Operation(summary = "저장된 계획의 전체 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansAllResponse> savedPlansAll(@AuthenticationPrincipal LoginMember loginMember,
                                                                @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        MyPagePlansAllResponse response = myPageService.getPlansAll(loginMember, sortBy, PlanStatus.SAVED);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared/plans/all")
    @Operation(summary = "저장된 계획의 전체 리스트를 나타내는 API")
    public ResponseEntity<MyPagePlansAllResponse> sharedPlansAll(@AuthenticationPrincipal LoginMember loginMember,
                                                                 @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        MyPagePlansAllResponse response = myPageService.getPlansAll(loginMember, sortBy, PlanStatus.SHARED);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/feedback")
    @Operation(summary = "마이페이지 개선 의견 남기기 기능")
    public ResponseEntity<Void> addFeedback(@AuthenticationPrincipal LoginMember loginMember,
                                            @RequestBody MemberFeedbackRequest request) {

        myPageService.addFeedback(loginMember, request);
        return ResponseEntity.noContent().build();
    }
}
