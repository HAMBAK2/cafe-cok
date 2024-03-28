package com.sideproject.hororok.utils.enums;

public enum OpenStatus {
    OPEN("영업중"), CLOSE("영업종료"), HOLY_DAY("금일휴업");

    private final String status;

    OpenStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
