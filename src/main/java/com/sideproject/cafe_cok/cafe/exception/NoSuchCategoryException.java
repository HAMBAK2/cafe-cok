package com.sideproject.cafe_cok.cafe.exception;

public class NoSuchCategoryException extends RuntimeException{

    public NoSuchCategoryException(final String message) {
        super(message);
    }

    public NoSuchCategoryException() {
        this("존재하지 않는 카테고리입니다..");
    }
}
