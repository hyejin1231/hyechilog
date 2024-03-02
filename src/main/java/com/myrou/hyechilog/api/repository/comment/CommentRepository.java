package com.myrou.hyechilog.api.repository.comment;

import com.myrou.hyechilog.api.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
