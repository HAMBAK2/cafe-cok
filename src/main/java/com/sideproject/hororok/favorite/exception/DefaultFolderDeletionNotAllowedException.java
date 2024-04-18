package com.sideproject.hororok.favorite.exception;

public class DefaultFolderDeletionNotAllowedException extends RuntimeException{

    public DefaultFolderDeletionNotAllowedException(final String message) {
        super(message);
    }

    public DefaultFolderDeletionNotAllowedException() {
        this("기본 폴더는 삭제할 수 없습니다.");
    }
}
