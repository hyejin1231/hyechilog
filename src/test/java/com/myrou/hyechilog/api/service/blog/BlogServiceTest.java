package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogServiceTest {

    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRepository blogRepository;

    @Test
    @DisplayName("게시글 저장한다.")
    void write() {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // when
        Blog blog = blogService.write(request);

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

}