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

    @Builder
    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
