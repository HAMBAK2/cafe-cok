package com.sideproject.cafe_cok.member.exception;

public class InvalidMemberException extends RuntimeException{

    public InvalidMemberException(final String message) {
        super(message);
    }

    public InvalidMemberException() {
        this("잘못된 회원 정보입니다.");
    }
}
