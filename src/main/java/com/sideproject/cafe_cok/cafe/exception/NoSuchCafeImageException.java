package com.sideproject.cafe_cok.cafe.exception;

public class NoSuchCafeImageException extends RuntimeException{

    public NoSuchCafeImageException(final String message) {
        super(message);
    }

    public NoSuchCafeImageException() {
        this("카페의 이미지가 존재하지 않습니다..");
    }
}
