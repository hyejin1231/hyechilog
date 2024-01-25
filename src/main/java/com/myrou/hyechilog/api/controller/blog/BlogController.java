package com.myrou.hyechilog.api.controller.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/v1/api")
@RestController
public class BlogController {

    /**
     * Blog create : 블로그 글 생성
     *
     * @param request
     * @return
     */
    @PostMapping("/createV1")
    public String createV1(@RequestBody BlogCreateRequest request) {
        log.info("request={}", request);
        return "create Blog Content";
    }

    @PostMapping("/create")
    public Map<String, String> create(@RequestBody @Valid BlogCreateRequest request, BindingResult result) {
        log.info("request={}", request);

        if (result.hasErrors()) { // request 검증
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);

            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);

            return error;
        }

        return Map.of();
    }
}
