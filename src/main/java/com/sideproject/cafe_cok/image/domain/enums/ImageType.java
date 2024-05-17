package com.sideproject.cafe_cok.image.domain.enums;

public enum ImageType {

    CAFE_MAIN_ORIGIN("카페_메인_원본_이미지"),
    CAFE_MAIN_MEDIUM("카페_메인_중간_이미지"),
    CAFE_MAIN_THUMBNAIL("카페_메인_썸네일_이미지"),
    CAFE_THUMBNAIL("카페_썸네일_이미지"),
    CAFE_ORIGIN("카페_원본_이미지"),

    MENU_ORIGIN("메뉴_원본_이미지"),
    MENU_THUMBNAIL("메뉴_썸네일_이미지"),

    REVIEW_ORIGIN("리뷰_원본_이미지"),
    REVIEW_THUMBNAIL("리뷰_썸네일_이미지");


    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
