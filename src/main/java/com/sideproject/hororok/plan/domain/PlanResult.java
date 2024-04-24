package com.sideproject.hororok.plan.domain;

public enum PlanResult {
    MATCH("일치"),
    SIMILAR("유사"),
    MISMATCH("불일치");

    private final String value;

    PlanResult(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}