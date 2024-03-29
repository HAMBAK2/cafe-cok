package com.sideproject.hororok.auth.controller;


import com.sideproject.hororok.auth.annotation.LoginUser;
import com.sideproject.hororok.auth.dto.SessionUser;
import com.sideproject.hororok.auth.kakao.dto.KakaoAccount;
import com.sideproject.hororok.auth.kakao.dto.KakaoInfo;
import com.sideproject.hororok.auth.kakao.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final KakaoService kakaoService;

    @GetMapping("/auth/kakao/login")
    public KakaoAccount kakaoLogin(@RequestParam("code") String code) {

        KakaoInfo kakaoInfo = kakaoService.getInfo(code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoInfo.getAccessToken());

        return kakaoInfo.getKakaoAccount();
    }
}
