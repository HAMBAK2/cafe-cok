package com.sideproject.cafe_cok.combination.exception;

public class NoSuchCombinationException extends RuntimeException{

    public NoSuchCombinationException(final String message) {
        super(message);
    }

    public NoSuchCombinationException() {
        this("존재하지 않는 조합입니다.");
    }
}
