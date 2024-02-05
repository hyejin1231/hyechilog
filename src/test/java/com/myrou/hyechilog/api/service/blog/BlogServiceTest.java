package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogServiceTest {

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    void setUp() {
        blogRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글 저장한다.")
    void write() {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // when
        BlogResponse blog = blogService.write(request);

        // then
        assertThat(blog)
                .extracting("title", "content").contains("테스트 제목", "테스트 내용");

        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(1)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        new Tuple("테스트 제목", "테스트 내용")
                );
    }

    @Test
    @DisplayName("글을 10개 한번에 작성후, 저장하면 10개 다 저장된다.")
    void writes() {
        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 11)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 [" + i + "]").build())
                .collect(Collectors.toList());

        List<BlogResponse> blogResponses = blogService.writes(blogCreateRequests);

        assertThat(blogResponses).hasSize(10);
    }

    @Test
    @DisplayName("글을 저장하고 그 글의 id 값으로 조회한다.")
    void get() {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("Hello")
                .content("World!")
                .build();

        BlogResponse givenBlog = blogService.write(request);

        // when
        BlogResponse whenBlog = blogService.get(givenBlog.getId());

        // then
        assertThat(whenBlog.getTitle()).isEqualTo(givenBlog.getTitle());
        assertThat(whenBlog.getContent()).isEqualTo(givenBlog.getContent());
    }

    @Test
    @DisplayName("글을 3개 저장하고 모든 글을 조회하면 3개의 글이 조회된다.")
    void getList() {
        // given
        BlogCreateRequest request1 = BlogCreateRequest.builder()
                .title("Hello1")
                .content("World!")
                .build();
        BlogCreateRequest request2 = BlogCreateRequest.builder()
                .title("Hello2")
                .content("World!")
                .build();
        BlogCreateRequest request3 = BlogCreateRequest.builder()
                .title("Hello3")
                .content("World!")
                .build();
        blogService.write(request1);
        blogService.write(request2);
        blogService.write(request3);

        // when
        List<BlogResponse> blogs = blogService.getList();

        // then
        assertThat(blogs).hasSize(3)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        new Tuple("Hello1", "World!"),
                        new Tuple("Hello2", "World!"),
                        new Tuple("Hello3", "World!")
                );
    }

    @Test
    @DisplayName("글을 30개 저장하고 첫 페이지를 조회하면 10개의 페이지가 조회된다.")
    void getListWithPaging() {
        // given
        List<Blog> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> Blog.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogRepository.saveAll(blogCreateRequests);

        // when
        List<BlogResponse> blogs = blogService.getListWithPaging(1);

        // then
        assertThat(blogs).hasSize(10);
        for (BlogResponse blog : blogs) {
            System.out.println("blog title : " + blog.getTitle() + ", content : " + blog.getContent());
        }
    }

    @Test
    @DisplayName("글을 30개 저장하고 첫 페이지를 조회하면 10개의 페이지가 조회된다.")
    void getListWithQueryDsl() {
        // given
        List<Blog> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> Blog.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogRepository.saveAll(blogCreateRequests);

        PageSearch pageSearch = PageSearch.builder().page(0).size(10).build();

        // when
        List<BlogResponse> blogs = blogService.getListWithQueryDsl(pageSearch);

        // then
        assertThat(blogs).hasSize(10);
        for (BlogResponse blog : blogs) {
            System.out.println("blog title : " + blog.getTitle() + ", content : " + blog.getContent());
        }
    }

}