package com.sideproject.cafe_cok.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminCafeFindResponse {

    private AdminCafeSaveResponse cafe;

    public AdminCafeFindResponse() {
        this.cafe = null;
    }

    public AdminCafeFindResponse(final AdminCafeSaveResponse cafe) {
        this.cafe = cafe;
    }

}
