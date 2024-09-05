package com.sideproject.cafe_cok.cafe.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class CafeSaveResponse extends RepresentationModel<CafeSaveResponse> {

    private String message;
    private String redirectUrl;

    public CafeSaveResponse(final String message,
                            final String redirectUrl) {
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
