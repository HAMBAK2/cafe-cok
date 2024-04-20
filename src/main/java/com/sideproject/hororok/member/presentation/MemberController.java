package com.sideproject.hororok.member.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.member.application.MemberService;
import com.sideproject.hororok.member.dto.response.MemberMyPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myPage")
    @Operation(summary = "마이 페이지를 눌렀을 때 동작")
    public ResponseEntity<MemberMyPageResponse> myPage(
            @AuthenticationPrincipal LoginMember loginMember) {

        MemberMyPageResponse response = memberService.myPage(loginMember);
        return ResponseEntity.ok(response);
    }
}
