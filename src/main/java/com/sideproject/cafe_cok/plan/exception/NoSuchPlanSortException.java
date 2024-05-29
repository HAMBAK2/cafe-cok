package com.sideproject.cafe_cok.plan.exception;

public class NoSuchPlanSortException extends RuntimeException{

    public NoSuchPlanSortException(final String message) {
        super(message);
    }

    public NoSuchPlanSortException() {
        this("존재하지 않는 타입의 정렬 기준입니다.");
    }
}
