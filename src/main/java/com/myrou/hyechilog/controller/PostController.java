package com.myrou.hyechilog.controller;

import com.myrou.hyechilog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker
            // -> html rendering
    // SPA -> vue
            // -> javascript + <-> api (json)
    // vue, nuxt (vue + SSR)
    // react, next (react + SSR)

    // HTTP Method => GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    // 이 특징을 다 알아야 한다!!
    @GetMapping("/v1/gets")
    public String get() {
        return "Hello world";
    }

    @PostMapping("/v1/posts")
//    public String post(@RequestParam("title") String title, @RequestParam("content") String content) { // 방법 1
//    public String post(@RequestParam Map<String, String> params) { // 방법2
    public String post(@ModelAttribute PostCreate params) { // 방법2
//        log.info("title={}, content={}", title, content);
        log.info("params={}", params);
        return "Hello Post World";
    }

    @PostMapping("/v1/posts2")
    public String post2(@RequestBody PostCreate params) { // 방법2
        log.info("params={}", params);
        return "Hello Post World";
    }

}
