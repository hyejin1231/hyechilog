package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogResponse write(BlogCreateRequest blogCreateRequest) {
        Blog blog = blogCreateRequest.toEntity(blogCreateRequest);
        return BlogResponse.of(blogRepository.save(blog));
    }

    public List<BlogResponse> writes(List<BlogCreateRequest> blogCreateRequests) {
        List<Blog> blogs = blogCreateRequests.stream().map(BlogCreateRequest::toEntity).collect(Collectors.toList());
        return blogRepository.saveAll(blogs).stream().map(BlogResponse::of).collect(Collectors.toList());
    }

    public BlogResponse get(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return BlogResponse.of(blog);

    }

    public List<BlogResponse> getList() {
        return blogRepository.findAll().stream().map(BlogResponse::of).collect(Collectors.toList());
    }

    public List<BlogResponse> getListWithPaging(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return blogRepository.findAll(pageable).stream().map(BlogResponse::of).collect(Collectors.toList());
    }
}
