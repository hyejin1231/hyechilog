package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.BlogEdit;
import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.BlogEditor;
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

    public List<BlogResponse> getListWithQueryDsl(PageSearch pageSearch) {
        return blogRepository.getList(pageSearch).stream().map(BlogResponse::of).collect(Collectors.toList());
    }
    
    public BlogResponse edit(long blogId, BlogEdit blogEdit)
    {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        
        // 1. 먼저 수정전 blog 글을 BlogEditorBuilder 에 값 세팅해준다.
        BlogEditor.BlogEditorBuilder blogEditor = blog.toEditor();
        
        // 2. 다음 이제 수정할 값이 들어있는 BlogEdit 값을 받아서 builder 에 값을 변경해준다.
        // 2-1. 이때 클라이언트에서 수정할 내용만 전달한다하면 제목 또는 내용 중에 null 값이 들어갈 수 있다.
        // 2-2. 이 문제를 해결하기 위해서 Builder를 lombok을 통해 사용하는 것이 아닌 직접 구현하는 걸로 해서 title(), content() 메서드 각각 호출할 때
        // blogEdit의 title, content의 null 값을 확인한 다음에 세팅하도록 한다. null 이면 기존 값 유지
        BlogEditor editor = blogEditor.title(blogEdit.getTitle()).content(blogEdit.getContent()).build();
        
        blog.edit(editor);
        
        return BlogResponse.of(blog);
    }
    
    public void delete(Long blogId)
    {
        Blog blog = blogRepository.findById(blogId).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        
        blogRepository.delete(blog);
    }
}
