package com.sideproject.hororok.member.presentation;


import com.amazonaws.Response;
import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.member.application.MyPageService;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/myPage")
@Tag(name = "MyPage", description = "마이페이지 관련 API")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 상단의 사용자 프로필을 나타냄")
    public ResponseEntity<MyPageProfileResponse> profile(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageProfileResponse response = myPageService.profile(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag/save")
    @Operation(summary = "마이페이지의 저장 태그를 눌렀을 때 동작")
    public ResponseEntity<MyPageTagSaveResponse> tagSave(@AuthenticationPrincipal LoginMember loginMember) {

        MyPageTagSaveResponse response = myPageService.tagSave(loginMember);
        return ResponseEntity.ok(response);
    }
}
