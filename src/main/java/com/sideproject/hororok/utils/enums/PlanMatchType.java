package com.sideproject.hororok.utils.enums;

public enum PlanMatchType {
    MATCH("일치"),
    SIMILAR("유사"),
    MISMATCH("불일치");

    private final String value;

    PlanMatchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}