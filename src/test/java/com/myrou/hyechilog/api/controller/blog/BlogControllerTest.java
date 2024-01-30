package com.myrou.hyechilog.api.controller.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.BlogService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;

@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogService blogService;

    @DisplayName("게시글 등록을 요청하면 create Blog Content가 출력된다.")
    @Test
    void createV1() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/createV1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("create Blog Content"))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시글 등록을 요청할 때 title 값이 없으면 '제목은 필수입니다.' 에러 메시지 출력된다.")
    @Test
    void createV2() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("")
                .content("내용입니다.")
                .build();

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/createV2")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("제목은 필수입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시글 등록을 요청할 때 title 값이 없으면 '제목은 필수입니다.' 에러 메시지 출력된다.")
    @Test
    void errorWhenNoTitle() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("")
                .content("내용입니다.")
                .build();

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/blogs/new")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("제목은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시글 등록을 요청할 때 content 값이 없으면 '내용은 필수입니다.' 에러 메시지 출력된다.")
    @Test
    void errorWhenNoContent() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("제목입니다.")
                .content("")
                .build();

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/blogs/new")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("내용은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시글 등록을 요청하면 DB에 게시글이 저장된다.")
    @Test
    void create() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("제목입니다!!")
                .content("내용입니다.!!")
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/blogs/new")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 작성 후 해당 id 값으로 조회하면 작성한 글을 조회한다.")
    void get() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("제목입니다!!")
                .content("내용입니다.!!")
                .build();

        Blog givenBlog = blogService.write(request);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/{blogId}", givenBlog.getId())
                .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(givenBlog.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(givenBlog.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }
}