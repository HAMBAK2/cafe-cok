package com.sideproject.cafe_cok.admin.exception;

public class NoWithdrawalMemberException extends RuntimeException{

    public NoWithdrawalMemberException(final String message) {
        super(message);
    }

    public NoWithdrawalMemberException() {
        this("탈퇴한 회원이 아닙니다.");
    }
}
