package com.sideproject.cafe_cok.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HttpHeadersUtil {

    @Value("${docs.swagger.base-uri}")
    private String swaggerBaseUri;

    public HttpHeaders createLinkHeaders(final String endPoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LINK, "<" + swaggerBaseUri + endPoint + ">; rel=\"profile\"");
        return headers;
    }
}
