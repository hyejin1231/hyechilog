package com.myrou.hyechilog.api.controller.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/v1/api")
@RestController
public class BlogController {

    /**
     * Blog create : 블로그 글 생성
     * @param request
     * @return
     */
    @PostMapping("/create")
    public String create(@RequestBody BlogCreateRequest request) {
        log.info("request={}", request);
        return "create Blog Content";
    }
}
