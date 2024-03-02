package com.myrou.hyechilog.api.controller.comment.request;

import lombok.Getter;

@Getter
public class CommentDeleteRequest {
    private String password;

    public CommentDeleteRequest() {
    }

    public CommentDeleteRequest(String password) {
        this.password = password;
    }
}
