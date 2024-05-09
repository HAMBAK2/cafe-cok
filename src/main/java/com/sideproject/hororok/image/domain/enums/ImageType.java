package com.sideproject.hororok.image.domain.enums;

public enum ImageType {


    CAFE_IMAGE("카페이미지"), REVIEW_IMAGE("리뷰이미지");


    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
