package com.sideproject.cafe_cok.plan.exception;

public class NoSuchPlanKeywordException extends RuntimeException{

    public NoSuchPlanKeywordException(final String message) {
        super(message);
    }

    public NoSuchPlanKeywordException() {
        this("계획에 목적 키워드가 존재하지 않습니다.");
    }
}
