package com.myrou.hyechilog.api.controller.comment.request;

import com.myrou.hyechilog.api.domain.comment.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentCreateRequest {

    @Length(min = 1, max = 10, message = "작성자는 1 ~ 8 글자로 입력해주세요.")
    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    @Length(min = 10, max = 10000, message = "내용은 10 ~ 1000 글자로 입력해주세요.")
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Length(min = 6, max = 30, message = "비밀번호는 6 ~ 30 글자로 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public CommentCreateRequest(String author, String content, String password) {
        this.author = author;
        this.content = content;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Comment toEntity(CommentCreateRequest request) {
        return Comment.builder()
                .author(request.getAuthor())
                .password(request.getPassword())
                .content(request.getContent())
                .build();
    }
}
