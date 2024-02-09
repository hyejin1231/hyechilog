package com.myrou.hyechilog.api.controller.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.BlogEdit;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        BlogResponse givenBlog = blogService.write(request);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/{blogId}", givenBlog.getId())
                .contentType(APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(givenBlog.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value(givenBlog.getContent()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 3개 작성 후 전체 조회를 하면 글 3개가 조회된다.")
    void getList() throws Exception {
        // given
        BlogCreateRequest request1= BlogCreateRequest.builder()
                .title("제목입니다11")
                .content("내용입니다.11")
                .build();
        BlogCreateRequest request2 = BlogCreateRequest.builder()
                .title("제목입니다22")
                .content("내용입니다.22")
                .build();
        BlogCreateRequest request3 = BlogCreateRequest.builder()
                .title("제목입니다33")
                .content("내용입니다.33")
                .build();
        blogService.write(request1);
        blogService.write(request2);
        blogService.write(request3);


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/old/blogs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목입니다11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].content").value("내용입니다.11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].title").value("제목입니다22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].content").value("내용입니다.22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].id").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].title").value("제목입니다33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].content").value("내용입니다.33"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 30개 작성 후 첫번째 페이지 조회하면 10개의 글이 조회된다.")
    void getListWithPaging() throws Exception {
        // given
        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogService.writes(blogCreateRequests);


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/old/paging/blogs?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목 [30]"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[9].title").value("제목 [21]"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 30개 작성 후 첫번째 페이지 조회하면 10개의 글이 조회된다.")
    void getListWithQueryDsl() throws Exception {
        // given
        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogService.writes(blogCreateRequests);


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs?page=0&size=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목 [30]"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[9].title").value("제목 [21]"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    
    @DisplayName("글 1개를 저장하고 글 제목을 수정하면 수정한 블로그 글을 조회할 수 있다.")
    @Test
    void editBlog() throws Exception
    {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("글 제목")
                .content("글 내용").build();
        
        BlogResponse response = blogService.write(request);
        
        BlogEdit blogEdit = BlogEdit.builder().title("글 제목 수정").content("글 내용").build();
        
        // when then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/blogs/{blogId}", response.getId())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(blogEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("글 제목 수정"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").value("글 내용"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    
    @DisplayName("게시글 1개를 작성 후 삭제하면 해당 글이 삭제된다.")
    @Test
    void delete() throws Exception
    {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder().title("게시글 제목")
                .content("게시글 내용").build();
        
        BlogResponse response = blogService.write(request);
        
        // when then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/blogs/{blogId}", response.getId())
                                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
         
    }

    @Test
    @DisplayName("존재하지 않는 글을 조회하면 예외가 발생한다.")
    void getNoBlog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/{blogId}",1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 글을 수정하면 예외가 발생한다.")
    void editNoBlog() throws Exception {
        BlogEdit blogEdit = BlogEdit.builder().title("글 제목 수정").content("글 내용").build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/blogs/{blogId}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blogEdit)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 글을 삭제하면 예외가 발생한다.")
    void deleteNoBlog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/blogs/{blogId}",1L)
                        .contentType(APPLICATION_JSON)
                        )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}