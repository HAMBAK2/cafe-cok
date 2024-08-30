package com.sideproject.cafe_cok.auth.exception;

public class NoSuchRefreshTokenException extends RuntimeException{

    public NoSuchRefreshTokenException(final String message) {
        super(message);
    }

    public NoSuchRefreshTokenException() {
        this("존재하지 않는 Token 입니다.");
    }
}
