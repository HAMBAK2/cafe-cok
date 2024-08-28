package com.sideproject.cafe_cok.plan.presentation;

import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.plan.dto.response.PlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanAllResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanPageResponse;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import com.sideproject.cafe_cok.plan.application.PlanService;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SavePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SharePlanRequest;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
@Tag(name = "Plan", description = "계획 관련 API")
public class PlanController {

    private final PlanService planService;
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "계획 저장 및 조회")
    public ResponseEntity<SavePlanResponse> plan(@RequestBody CreatePlanRequest request,
                                                 HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        SavePlanResponse response = planService.doPlan(request, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{planId}")
    @Operation(summary = "planId에 해당하는 계획 조회")
    public ResponseEntity<PlanResponse> findPlan(@AuthenticationPrincipal LoginMember loginMember,
                                                 @PathVariable Long planId){

        PlanResponse response = planService.findPlan(loginMember, planId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    @Operation(summary = "계획 상태 저장으로 수정")
    public ResponseEntity<PlanIdResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                 @RequestBody SavePlanRequest request) {

        PlanIdResponse response = planService.save(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/share")
    @Operation(summary = "계획 상태 공유로 수정")
    public ResponseEntity<PlanIdResponse> share(@AuthenticationPrincipal LoginMember loginMember,
                                                @RequestBody SharePlanRequest request) {

        PlanIdResponse response = planService.share(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "planId에 해당하는 계획 삭제")
    public ResponseEntity<PlanIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                     @PathVariable Long planId,
                                                     @RequestParam PlanStatus status){

        PlanIdResponse response = planService.delete(status, planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared")
    @Operation(summary = "공유된 계획 조회(페이징)")
    public ResponseEntity<PlanPageResponse> sharedPlans(@AuthenticationPrincipal LoginMember loginMember,
                                                        @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer size) {

        PlanPageResponse response = planService.getPlans(loginMember, sortBy, PlanStatus.SHARED, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved")
    @Operation(summary = "저장된 계획 조회(페이징)")
    public ResponseEntity<PlanPageResponse> savedPlans(@AuthenticationPrincipal LoginMember loginMember,
                                                       @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy,
                                                       @RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size) {

        PlanPageResponse response = planService.getPlans(loginMember, sortBy, PlanStatus.SAVED, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved/all")
    @Operation(summary = "저장된 계획 조회(전체)")
    public ResponseEntity<PlanAllResponse> savedPlansAll(@AuthenticationPrincipal LoginMember loginMember,
                                                         @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        PlanAllResponse response = planService.getPlansAll(loginMember, sortBy, PlanStatus.SAVED);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shared/all")
    @Operation(summary = "공유된 계획 조회(전체)")
    public ResponseEntity<PlanAllResponse> sharedPlansAll(@AuthenticationPrincipal LoginMember loginMember,
                                                          @RequestParam(defaultValue = "RECENT") PlanSortBy sortBy) {

        PlanAllResponse response = planService.getPlansAll(loginMember, sortBy, PlanStatus.SHARED);
        return ResponseEntity.ok(response);
    }

    private Long getMemberId(final HttpServletRequest request) {

        try {
            String accessToken = AuthorizationExtractor.extract(request);
            Long memberId = authService.extractMemberId(accessToken);
            return memberId;
        } catch (final InvalidTokenException | EmptyAuthorizationHeaderException |
                       NoSuchMemberException e) {
            return null;
        }
    }

}
