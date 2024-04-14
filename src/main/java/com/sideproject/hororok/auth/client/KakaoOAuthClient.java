package com.sideproject.hororok.auth.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.hororok.auth.application.OAuthClient;
import com.sideproject.hororok.global.config.properties.KakaoProperties;
import com.sideproject.hororok.auth.dto.OAuthMember;
import com.sideproject.hororok.auth.dto.UserInfo;
import com.sideproject.hororok.auth.dto.response.KakaoTokenResponse;
import com.sideproject.hororok.auth.dto.response.OAuthAccessTokenResponse;
import com.sideproject.hororok.auth.exception.OAuthException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class KakaoOAuthClient implements OAuthClient {

    private static final String JWT_DELIMITER = "\\.";

    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    public KakaoOAuthClient(final KakaoProperties properties,
                            final ObjectMapper objectMapper,
                            final RestTemplateBuilder restTemplateBuilder) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public OAuthMember getOAuthMember(String code) {

        KakaoTokenResponse kakaoTokenResponse = requestKakaoToken(code);
        String payload = getPayload(kakaoTokenResponse.getIdToken());
        UserInfo userInfo = parseUserInfo(payload);

        String refreshToken = kakaoTokenResponse.getRefreshToken();
        return new OAuthMember(userInfo.getEmail(), userInfo.getNickname(), refreshToken);
    }

    @Override
    public OAuthAccessTokenResponse getAccessToken(String refreshToken) {
        return null;
    }

    private KakaoTokenResponse requestKakaoToken(final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = generateTokenParams(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return fetchKakaoToken(request).getBody();
    }

    private MultiValueMap<String, String> generateTokenParams(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", properties.getGrantType());
        params.add("client_id", properties.getClientId());
        params.add("redirect_uri", properties.getRedirectUri());
        params.add("code", code);

        return params;
    }

    private ResponseEntity<KakaoTokenResponse> fetchKakaoToken(
            final HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForEntity(properties.getBaseUri(), request, KakaoTokenResponse.class);
        } catch (final RestClientException e) {
            throw new OAuthException(e);
        }
    }

    private String getPayload(final String jwt) {
        return jwt.split(JWT_DELIMITER)[1];
    }

    private UserInfo parseUserInfo(final String payload) {
        String decodedPayload = decodeJwtPayload(payload);
        try {
            return objectMapper.readValue(decodedPayload, UserInfo.class);
        } catch (final JsonProcessingException e) {
            throw new OAuthException("id 토큰을 읽을 수 없습니다.", e);
        }
    }

    private String decodeJwtPayload(final String payload) {
        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }
}
