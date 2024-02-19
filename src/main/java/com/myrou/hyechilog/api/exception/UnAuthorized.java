package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorized extends BlogException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public UnAuthorized() {
        super(MESSAGE);
    }
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
