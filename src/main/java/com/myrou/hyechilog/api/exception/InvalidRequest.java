package com.myrou.hyechilog.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequest extends BlogException{
    private static final String MESSAGE = "해당 글이 존재하지 않습니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String errorMessage) {
        super(MESSAGE);
        addValidation(fieldName, errorMessage);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
