package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidPassword extends BlogException{
    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";
    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
