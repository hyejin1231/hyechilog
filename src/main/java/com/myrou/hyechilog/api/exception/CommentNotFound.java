package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFound extends BlogException{

    private static final String MESSAGE = "댓글이 존재하지 않습니다.";
    public CommentNotFound() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
