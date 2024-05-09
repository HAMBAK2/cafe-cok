package com.sideproject.cafe_cok.cafe.exception;

public class InvalidCafeException extends RuntimeException{

    public InvalidCafeException(final String message) {
        super(message);
    }

    public InvalidCafeException() {
        this("잘못된 카페 정보입니다.");
    }
}
