package com.myrou.hyechilog.api.controller.blog;

import com.myrou.hyechilog.api.exception.BlogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse invalidException(MethodArgumentNotValidException e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
    }

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ApiResponse> blogException(BlogException e) {
        ApiResponse<Object> response = ApiResponse.of(e.getStatus(), e.getMessage(), e.getValidation());

        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
