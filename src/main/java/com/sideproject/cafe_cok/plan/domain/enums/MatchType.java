package com.sideproject.cafe_cok.plan.domain.enums;

public enum MatchType {
    MATCH("일치"),
    SIMILAR("유사"),
    MISMATCH("불일치");

    private final String value;

    MatchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}