package com.sideproject.hororok.plan.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.plan.dto.request.CreatePlanRequest;
import com.sideproject.hororok.plan.dto.request.SavePlanRequest;
import com.sideproject.hororok.plan.dto.request.SharePlanRequest;
import com.sideproject.hororok.plan.dto.response.CreatePlanResponse;
import com.sideproject.hororok.plan.application.PlanService;
import com.sideproject.hororok.plan.dto.response.SavePlanResponse;
import com.sideproject.hororok.plan.dto.response.SharePlanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
@Tag(name = "Plan", description = "계획하기 관련 API")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(summary = "계획하기를 통해 선택한 항목에 대한 결과를 제공")
    public ResponseEntity<CreatePlanResponse> plan(@RequestBody CreatePlanRequest request) {

        CreatePlanResponse response = planService.plan(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/save")
    @Operation(summary = "계획하기 결과를 저장")
    public ResponseEntity<SavePlanResponse> save(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody SavePlanRequest request) {

        SavePlanResponse response = planService.save(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/share")
    @Operation(summary = "계획하기 결과를 공유")
    public ResponseEntity<SharePlanResponse> share(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody SharePlanRequest request) {

        SharePlanResponse response = planService.share(request, loginMember);
        return ResponseEntity.ok(response);
    }

}
