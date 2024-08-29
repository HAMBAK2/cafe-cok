package com.sideproject.cafe_cok.member.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.member.application.MemberService;
import com.sideproject.cafe_cok.member.dto.request.MemberFeedbackRequest;
import com.sideproject.cafe_cok.member.dto.response.MemberResponse;
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
@RequestMapping("/api/member")
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 조회")
    public ResponseEntity<MemberResponse> find(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse response = memberService.find(loginMember);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 수정")
    public ResponseEntity<MemberResponse> edit(@AuthenticationPrincipal LoginMember loginMember,
                                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                                      @RequestParam("nickname") String nickname) throws IOException {

        MemberResponse response = memberService.edit(loginMember, nickname, file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    @Operation(summary = "회원 개선의견 저장")
    public ResponseEntity<Void> saveFeedback(@AuthenticationPrincipal LoginMember loginMember,
                                             @RequestBody MemberFeedbackRequest request) {

        memberService.saveFeedback(loginMember, request);
        return ResponseEntity.noContent().build();
    }


}
