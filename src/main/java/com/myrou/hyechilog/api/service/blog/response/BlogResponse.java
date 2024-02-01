package com.myrou.hyechilog.api.service.blog.response;

import com.myrou.hyechilog.api.domain.blog.Blog;
import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 응답 클래스 분리 !
 * -> 정책에 맞게 분리하기
 * -> ex) title은 10 글자까지만!! 저장되도록 (이건 예시일뿐,, 사실 글자수 같은 건 프론트에서 하는게 맞음 !!
 */
@Getter
public class BlogResponse {
    private Long id;
    private String title;
    private String content;

    @Builder
    public BlogResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static BlogResponse of(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .build();
    }
}
