package com.sideproject.hororok.plan.presentation;


import com.sideproject.hororok.cafe.dto.request.CreatePlanRequest;
import com.sideproject.hororok.cafe.dto.response.CreatePlanResponse;
import com.sideproject.hororok.plan.domain.application.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
