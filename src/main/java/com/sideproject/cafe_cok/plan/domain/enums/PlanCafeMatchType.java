package com.sideproject.cafe_cok.plan.domain.enums;

public enum PlanCafeMatchType {

    MATCH("일치"),
    SIMILAR("유사");

    private final String value;

    PlanCafeMatchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
