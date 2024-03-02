package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class UserNotFound extends BlogException{

    private static final String MESSAGE = "존재하지 않는 사용자입니다.";
    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
