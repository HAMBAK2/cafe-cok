package com.sideproject.cafe_cok.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminSuccessAndRedirectResponse {

    private String message;
    private String redirectUrl;

    public AdminSuccessAndRedirectResponse(final String message,
                                           final String redirectUrl) {
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
