package com.sideproject.cafe_cok.auth.exception;

import java.time.LocalDateTime;

public class InvalidRestoreMemberException extends RuntimeException{

    public InvalidRestoreMemberException(final String message) {
        super(message);
    }

    public InvalidRestoreMemberException(final LocalDateTime localDateTime) {
        super("회원 탈퇴 후 7일이 지나야 재가입이 가능합니다.(탈퇴 일시 : " + localDateTime + ")");
    }

    public InvalidRestoreMemberException() {
        this("회원 탈퇴 후 7일이 지나야 재가입이 가능합니다.");
    }
}
