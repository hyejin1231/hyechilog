package com.myrou.hyechilog.api.controller.comment;

import com.myrou.hyechilog.api.controller.blog.ApiResponse;
import com.myrou.hyechilog.api.controller.comment.request.CommentCreateRequest;
import com.myrou.hyechilog.api.controller.comment.request.CommentDeleteRequest;
import com.myrou.hyechilog.api.service.comment.CommentService;
import com.myrou.hyechilog.api.service.comment.response.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/blog/{blogId}/comments")
    public ApiResponse<List<CommentResponse>> writeComment(@PathVariable Long blogId, @RequestBody @Valid CommentCreateRequest request) {
        List<CommentResponse> commentResponses = commentService.writeComment(blogId, request);

        return ApiResponse.ok(commentResponses);
    }

    @PostMapping("/comments/{commentId}/delete")
    public ApiResponse deleteComment(@PathVariable Long commentId, @RequestBody @Valid CommentDeleteRequest request) {
        commentService.deleteComment(commentId, request);

        return ApiResponse.ok(null);
    }

}
