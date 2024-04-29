package com.sideproject.hororok.keword.exception;

public class NoSuchKeywordException extends RuntimeException{

    public NoSuchKeywordException(final String message) {
        super(message);
    }

    public NoSuchKeywordException() {
        this("존재하지 않는 키워드입니다.");
    }
}
