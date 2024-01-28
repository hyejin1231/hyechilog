package com.myrou.hyechilog.api.controller.blog.request;

import com.myrou.hyechilog.api.domain.blog.Blog;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class BlogCreateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public BlogCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Blog toEntity(BlogCreateRequest request) {
        return Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
