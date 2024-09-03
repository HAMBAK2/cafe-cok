package com.sideproject.cafe_cok.combination.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.combination.application.CombinationService;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/combinations")
@Tag(name = "Combination", description = "조합 API")
public class CombinationController {

    private final CombinationService combinationService;

    @GetMapping
    @Operation(summary = "조합 목록 조회")
    public ResponseEntity<CombinationListResponse> combination(@AuthenticationPrincipal LoginMember loginMember) {

        CombinationListResponse response = combinationService.combination(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "조합 저장")
    public ResponseEntity<CombinationIdResponse> create(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody CombinationRequest request) {

        CombinationIdResponse response = combinationService.create(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{combinationId}")
    @Operation(summary = "combinationId에 해당하는 조합 조회")
    public ResponseEntity<CombinationResponse> detail(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long combinationId) {

        CombinationResponse response = combinationService.detail(combinationId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{combinationId}")
    @Operation(summary = "combinationId에 해당하는 조합 수정")
    public ResponseEntity<CombinationIdResponse> edit(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long combinationId, @RequestBody CombinationRequest request) {

        CombinationIdResponse response = combinationService.edit(request, combinationId);
        return ResponseEntity.ok(response);
    }

}
