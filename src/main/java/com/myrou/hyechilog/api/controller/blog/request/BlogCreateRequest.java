package com.myrou.hyechilog.api.controller.blog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class BlogCreateRequest {
    private String title;
    private String content;

    @Builder
    public BlogCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
