package com.sideproject.hororok.plan.domain.enums;

public enum PlanStatus {

    SAVED("저장"),
    SHARED("공유");

    private final String value;

    PlanStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
