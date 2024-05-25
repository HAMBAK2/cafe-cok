package com.sideproject.cafe_cok.utils.tmap.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.cafe_cok.utils.tmap.exception.TmapException;
import com.sideproject.cafe_cok.global.config.properties.TmapProperties;
import com.sideproject.cafe_cok.utils.tmap.dto.response.TmapApiResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;


@Component
public class TmapClient {

    private final TmapProperties properties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final Integer X_NUM_DIGITS = 3;
    private static final Integer Y_NUM_DIGITS = 2;
    private static final String START_NAME = "start";
    private static final String END_NAME = "end";
    private static final String NEAR_ERROR_CODE = "1007";



    public TmapClient(final TmapProperties properties,
                      final ObjectMapper objectMapper,
                      final RestTemplateBuilder restTemplateBuilder) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.build();
    }


    public Integer getWalkingTime(
            final BigDecimal startX, final BigDecimal startY, final BigDecimal endX, final BigDecimal endY) {

        HttpHeaders headers = generateTmapHeader();
        String body = generateTmapBody(startX, startY, endX, endY);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        TmapApiResponse tmapApiResponse = fetchTmapApi(request);
        int totalTime = 0;
        if(tmapApiResponse.getFeatures() == null) return totalTime;

        totalTime = tmapApiResponse.getFeatures().get(0).getProperties().getTotalTime();
        return convertSecondsToMinutes(totalTime);
    }

    private TmapApiResponse fetchTmapApi(final HttpEntity<String> request) {
        try {
            return restTemplate.postForObject(properties.getBaseUri(), request, TmapApiResponse.class);
        } catch (final RestClientException e) {
            if(e.getMessage().contains(NEAR_ERROR_CODE)) {
                return new TmapApiResponse();
            }
            throw new TmapException(e);
        }
    }

    private HttpHeaders generateTmapHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set("appKey", properties.getAppKey());
        return headers;
    }

    private String generateTmapBody(
            final BigDecimal startX, final BigDecimal startY, final BigDecimal endX, final BigDecimal endY) {

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("startX", startX);
        bodyMap.put("startY", startY);
        bodyMap.put("endX", endX);
        bodyMap.put("endY", endY);
        bodyMap.put("startName", START_NAME);
        bodyMap.put("endName", END_NAME);

        try {
            return objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert body to JSON string", e);
        }
    }

}
