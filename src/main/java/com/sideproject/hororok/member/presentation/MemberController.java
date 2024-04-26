package com.sideproject.hororok.member.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.member.application.MemberService;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/myPage/plan/{planId}")
    @Operation(summary = "마이페이지 계획탭에서 하나의 계획(여정)을 선택했을 때 동작")
    public ResponseEntity<MyPagePlanDetailResponse> planDetail(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long planId){

        MyPagePlanDetailResponse response = memberService.planDetail(planId);
        return ResponseEntity.ok(response);
    }

}
