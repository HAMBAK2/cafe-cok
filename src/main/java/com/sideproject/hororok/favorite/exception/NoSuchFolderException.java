package com.sideproject.hororok.favorite.exception;

public class NoSuchFolderException extends RuntimeException{

    public NoSuchFolderException(final String message) {
        super(message);
    }

    public NoSuchFolderException() {
        this("존재하지 않는 폴더입니다.");
    }
}
