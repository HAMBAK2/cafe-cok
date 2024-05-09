package com.sideproject.cafe_cok.plan.domain.enums;

public enum PlanSortBy {

    RECENT("createdDate"),
    UPCOMING("visitDate");

    private final String value;

    PlanSortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
