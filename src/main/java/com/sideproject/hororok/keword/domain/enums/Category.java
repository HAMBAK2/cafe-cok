package com.sideproject.hororok.keword.domain.enums;

public enum Category {

    PURPOSE("목적"),
    MENU("메뉴"),
    THEME("테마"),
    ATMOSPHERE("분위기"),
    FACILITY("시설");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
