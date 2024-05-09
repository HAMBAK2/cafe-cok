package com.sideproject.cafe_cok.cafe.domain.enums;

public enum OpenStatus {

    OPEN("영업중"), CLOSE("영업종료"), HOLY_DAY("휴무일");


    private final String value;

    OpenStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
