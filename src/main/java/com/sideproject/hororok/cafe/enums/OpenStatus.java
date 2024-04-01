package com.sideproject.hororok.cafe.enums;

public enum OpenStatus {

    OPEN("영업중"), CLOSE("영업종료"), HOLY_DAY("휴무일");

    private final String description;

    OpenStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
