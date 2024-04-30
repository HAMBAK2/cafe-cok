package com.sideproject.hororok.combination.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.combination.application.CombinationService;
import com.sideproject.hororok.combination.dto.request.CombinationCreateRequest;
import com.sideproject.hororok.combination.dto.response.CombinationCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/combination")
@Tag(name = "Combination", description = "조합 관련 API")
public class CombinationController {

    private final CombinationService combinationService;

    @PostMapping("/create")
    @Operation(summary = "조합을 생성하는 기능")
    public ResponseEntity<CombinationCreateResponse> create(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody CombinationCreateRequest request) {

        CombinationCreateResponse response = combinationService.create(request, loginMember);
        return ResponseEntity.ok(response);
    }
}
