package com.sideproject.hororok.auth.kakao.service;

import java.net.URI;

import com.sideproject.hororok.auth.kakao.client.KakaoClient;
import com.sideproject.hororok.auth.kakao.dto.KakaoAccount;
import com.sideproject.hororok.auth.kakao.dto.KakaoInfo;
import com.sideproject.hororok.auth.kakao.dto.KakaoToken;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient client;

    @Value("${kakao.auth-url}")
    private String kakaoAuthUrl;

    @Value("${kakao.user-api-url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.restapi-key}")
    private String restapiKey;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @Value("${kakao.logout-url}")
    private String logoutUrl;

    public KakaoInfo getInfo(final KakaoToken token) {
        log.debug("token = {}", token);
        try {

            return client.getInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken());
        } catch (Exception e) {
            log.error("something error..", e);
            return KakaoInfo.fail();
        }
    }

    public KakaoToken getToken(final String code) {
        try {
            return client.getToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KakaoToken.fail();
        }
    }
}