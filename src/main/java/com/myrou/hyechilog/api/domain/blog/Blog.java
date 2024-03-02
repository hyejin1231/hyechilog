package com.myrou.hyechilog.api.domain.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blog")
    private List<Comment> comments;

    @Builder
    public Blog(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    
    public BlogEditor.BlogEditorBuilder toEditor()
    {
        return BlogEditor.builder()
                .title(title)
                .content(content);
    }
    
    public void edit(BlogEditor blogEditor)
    {
        this.title = blogEditor.getTitle();
        this.content = blogEditor.getContent();
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public List<Comment> addComment(Comment comment) {
        comment.setBlog(this);
        this.comments.add(comment);
        return this.comments;
    }
}
