package com.sideproject.cafe_cok.bookmark.exception;

public class DefaultFolderUpdateNotAllowedException extends RuntimeException{

    public DefaultFolderUpdateNotAllowedException(final String message) {
        super(message);
    }

    public DefaultFolderUpdateNotAllowedException() {
        this("기본 폴더의 이름과 색상은 변경할 수 없습니다.");
    }
}
