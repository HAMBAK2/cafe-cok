package com.sideproject.cafe_cok.menu.exception;

public class NoSuchMenuException extends RuntimeException{

    public NoSuchMenuException(final String message) {
        super(message);
    }

    public NoSuchMenuException() {
        this("존재하지 않는 카페입니다.");
    }
}
