package com.sideproject.hororok.utils.tmap.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject.hororok.global.config.properties.TmapProperties;
import com.sideproject.hororok.utils.tmap.dto.response.TmapApiResponse;
import com.sideproject.hororok.utils.tmap.exception.TmapException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.sideproject.hororok.utils.FormatConverter.*;


@Component
public class TmapClient {

    private final TmapProperties properties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final Integer X_NUM_DIGITS = 3;
    private static final Integer Y_NUM_DIGITS = 2;
    private static final String START_NAME = "start";
    private static final String END_NAME = "end";



    public TmapClient(final TmapProperties properties,
                      final ObjectMapper objectMapper,
                      final RestTemplateBuilder restTemplateBuilder) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplateBuilder.build();
    }

    public TmapApiResponse requestTmapApi(
            final Integer startX, final Integer startY, final Integer endX, final Integer endY) {

        HttpHeaders headers = generateTmapHeader();
        String body = generateTmapBody(startX, startY, endX, endY);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        return fetchTmapApi(request);
    }

    private TmapApiResponse fetchTmapApi(final HttpEntity<String> request) {
        try {
            return restTemplate.postForObject(properties.getBaseUri(), request, TmapApiResponse.class);
        } catch (final RestClientException e) {
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
           final Integer startX, final Integer startY, final Integer endX, final Integer endY) {

       Map<String, Object> bodyMap = new HashMap<>();
       bodyMap.put("startX", convertToDecimal(startX, X_NUM_DIGITS));
       bodyMap.put("startY", convertToDecimal(startY, Y_NUM_DIGITS));
       bodyMap.put("speed", properties.getSpeed());
       bodyMap.put("endX", convertToDecimal(endX, X_NUM_DIGITS));
       bodyMap.put("endY", convertToDecimal(endY, Y_NUM_DIGITS));
       bodyMap.put("startName", encodeUtf8(START_NAME));
       bodyMap.put("endName", encodeUtf8(END_NAME));

       try {
           return objectMapper.writeValueAsString(bodyMap);
       } catch (JsonProcessingException e) {
           throw new RuntimeException("Failed to convert body to JSON string", e);
       }
   }

}
