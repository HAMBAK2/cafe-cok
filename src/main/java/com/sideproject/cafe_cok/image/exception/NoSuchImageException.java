package com.sideproject.cafe_cok.image.exception;

public class NoSuchImageException extends RuntimeException{

    public NoSuchImageException(final String message) {
        super(message);
    }

    public NoSuchImageException() {
        this("해당하는 이미지가 존재하지 않습니다.");
    }
}
