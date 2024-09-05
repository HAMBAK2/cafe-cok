package com.sideproject.cafe_cok.util.S3.exception;

public class FileUploadException extends RuntimeException{

    public FileUploadException(final String message) {
        super(message);
    }

    public FileUploadException() {
        this("파일을 업로드 하는 중 에러가 발생했습니다.");
    }
}
