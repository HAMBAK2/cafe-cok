package com.sideproject.hororok.cafe.exception;

public class InvalidCafeException extends RuntimeException{

    public InvalidCafeException(final String message) {
        super(message);
    }

    public InvalidCafeException() {
        this("잘못된 카페 정보입니다.");
    }
}
