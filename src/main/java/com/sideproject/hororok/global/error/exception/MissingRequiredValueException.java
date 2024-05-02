package com.sideproject.hororok.global.error.exception;

public class MissingRequiredValueException extends RuntimeException{

    public MissingRequiredValueException(final String message) {
        super("필수 값이 누락 되었습니다. (누락된 값 : " + message + ")");
    }

    public MissingRequiredValueException() {
        this("필수 값이 누락되었습니다.");
    }
}
