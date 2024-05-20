package com.sideproject.cafe_cok.image.domain.enums;

public enum ImageType {

    CAFE_MAIN("카페 메인"),
    CAFE("카페"),
    REVIEW("리뷰"),
    MENU("메뉴");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
