package com.sideproject.cafe_cok.review.exception;

public class NoSuchReviewException extends RuntimeException{

    public NoSuchReviewException(final String message) {
        super(message);
    }

    public NoSuchReviewException() {
        this("존재하지 않는 리뷰입니다.");
    }
}
