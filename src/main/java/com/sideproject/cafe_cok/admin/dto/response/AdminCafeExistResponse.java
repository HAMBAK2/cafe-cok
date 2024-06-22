package com.sideproject.cafe_cok.admin.dto.response;

import lombok.Getter;

@Getter
public class AdminCafeExistResponse {

    private boolean isExist;

    public AdminCafeExistResponse(final boolean isExist) {
        this.isExist = isExist;
    }
}
