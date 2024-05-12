package com.sideproject.cafe_cok.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminMenuSaveRequest {

    @NotNull
    private String name;
    private Integer price;
    private String image;

    protected AdminMenuSaveRequest() {}

    public AdminMenuSaveRequest(final String name, final Integer price, final String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

}
