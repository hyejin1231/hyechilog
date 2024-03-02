package com.myrou.hyechilog.api.service.comment;

import com.myrou.hyechilog.api.controller.blog.ApiResponse;
import com.myrou.hyechilog.api.controller.comment.request.CommentCreateRequest;
import com.myrou.hyechilog.api.controller.comment.request.CommentDeleteRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.comment.Comment;
import com.myrou.hyechilog.api.exception.BlogNotFound;
import com.myrou.hyechilog.api.exception.CommentNotFound;
import com.myrou.hyechilog.api.exception.InvalidPassword;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.repository.comment.CommentRepository;
import com.myrou.hyechilog.api.service.comment.response.CommentResponse;
import com.myrou.hyechilog.support.crypto.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public List<CommentResponse> writeComment(Long blogId, CommentCreateRequest request) {
        // 1) 블로그 글 존재 여부 확인
        Blog blog = blogRepository.findById(blogId).orElseThrow(BlogNotFound::new);

        request.setPassword(passwordEncoder.encrypt(request.getPassword()));

        // 2)
        Comment comment = CommentCreateRequest.toEntity(request);
        List<Comment> comments = blog.addComment(comment);

        return comments.stream().map(CommentResponse::of).collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, CommentDeleteRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

        if (!passwordEncoder.matches(request.getPassword(), comment.getPassword())) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
