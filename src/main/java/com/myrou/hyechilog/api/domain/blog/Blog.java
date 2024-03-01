package com.myrou.hyechilog.api.domain.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
