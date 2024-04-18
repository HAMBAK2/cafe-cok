package com.sideproject.hororok.cafe.exception;

public class NoSuchCafeException extends RuntimeException{

    public NoSuchCafeException(final String message) {
        super(message);
    }

    public NoSuchCafeException() {
        this("존재하지 않는 카페입니다.");
    }
}
