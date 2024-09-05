package com.sideproject.cafe_cok.plan.presentation;

import com.sideproject.cafe_cok.auth.application.AuthService;
import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.exception.EmptyAuthorizationHeaderException;
import com.sideproject.cafe_cok.auth.exception.InvalidTokenException;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.auth.presentation.AuthorizationExtractor;
import com.sideproject.cafe_cok.plan.dto.response.PlanResponse;
import com.sideproject.cafe_cok.member.exception.NoSuchMemberException;
import com.sideproject.cafe_cok.plan.application.PlanService;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.dto.request.PlanSaveRequest;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.PlanIdResponse;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@Tag(name = "plans", description = "계획 관련 API")
public class PlanController {

    private final PlanService planService;
    private final AuthService authService;
    private final HttpHeadersUtil httpHeadersUtil;

    @PostMapping
    @Operation(summary = "계획 저장")
    public ResponseEntity<SavePlanResponse> save(@RequestBody PlanSaveRequest request,
                                                 HttpServletRequest servletRequest) {

        Long memberId = getMemberId(servletRequest);
        SavePlanResponse response = planService.save(request, memberId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("plans/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{planId}")
    @Operation(summary = "planId에 해당하는 계획 조회")
    public ResponseEntity<PlanResponse> find(@AuthenticationPrincipal LoginMember loginMember,
                                             @PathVariable Long planId){

        PlanResponse response = planService.find(loginMember, planId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("plans/find");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PatchMapping("/{planId}")
    @Operation(summary = "planId에 해당하는 계획 수정")
    public ResponseEntity<PlanIdResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                 @PathVariable Long planId,
                                                 @RequestParam PlanStatus status) {

        PlanIdResponse response = planService.update(status, planId, loginMember);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("plans/update");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "planId에 해당하는 계획 삭제")
    public ResponseEntity<PlanIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                 @PathVariable Long planId,
                                                 @RequestParam PlanStatus status){

        PlanIdResponse response = planService.delete(status, planId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("plans/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
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
