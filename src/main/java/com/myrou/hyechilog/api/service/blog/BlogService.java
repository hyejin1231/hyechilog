package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogResponse write(BlogCreateRequest blogCreateRequest) {
        Blog blog = blogCreateRequest.toEntity(blogCreateRequest);
        return BlogResponse.of(blogRepository.save(blog));
    }

    public BlogResponse get(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return BlogResponse.of(blog);

    }
}
