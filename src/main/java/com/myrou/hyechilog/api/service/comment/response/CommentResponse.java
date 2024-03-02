package com.myrou.hyechilog.api.service.comment.response;

import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.comment.Comment;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {
    private String author;
    private String content;

    @Builder
    public CommentResponse(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .author(comment.getAuthor())
                .content(comment.getContent())
                .build();
    }
}
