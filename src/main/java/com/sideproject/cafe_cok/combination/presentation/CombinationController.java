package com.sideproject.cafe_cok.combination.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.combination.application.CombinationService;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationDetailResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/combination")
@Tag(name = "Combination", description = "조합 API")
public class CombinationController {

    private final CombinationService combinationService;

    @PostMapping
    @Operation(summary = "조합 저장")
    public ResponseEntity<CombinationIdResponse> create(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody CombinationRequest request) {

        CombinationIdResponse response = combinationService.create(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{combinationId}")
    @Operation(summary = "combinationId에 해당하는 조합 조회")
    public ResponseEntity<CombinationDetailResponse> detail(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long combinationId) {

        CombinationDetailResponse response = combinationService.detail(combinationId);
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
