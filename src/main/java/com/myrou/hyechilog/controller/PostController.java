package com.myrou.hyechilog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker
            // -> html rendering
    // SPA -> vue
            // -> javascript + <-> api (json)
    // vue, nuxt (vue + SSR)
    // react, next (react + SSR)
    @GetMapping("/v1/posts")
    public String get() {
        return "Hello world";
    }
}
