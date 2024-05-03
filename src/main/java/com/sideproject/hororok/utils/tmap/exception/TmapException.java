package com.sideproject.hororok.utils.tmap.exception;

public class TmapException extends RuntimeException{

    public TmapException() {
        super("Tmap 서버와의 통신 과정에서 문제가 발생했습니다.");
    }

    public TmapException(final Exception e) {
        this("Tmap 서버와의 통신 과정에서 문제가 발생했습니다.", e);
    }

    public TmapException(final String message, final Exception e) {
        super(message, e);
    }
}
