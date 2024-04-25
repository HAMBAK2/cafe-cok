package com.sideproject.hororok.plan.exception;

public class NoSuchPlanException extends RuntimeException{

    public NoSuchPlanException(final String message) {
        super(message);
    }

    public NoSuchPlanException() {
        this("존재하지 않는 계획입니다.");
    }
}
