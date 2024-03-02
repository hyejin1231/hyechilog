package com.myrou.hyechilog.api.controller.blog.request;

import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.InvalidRequest;
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

    public static Blog toEntity(BlogCreateRequest request, User user) {
        return Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 '바보'를 포함할 수 없습니다.");
        }
    }
}
