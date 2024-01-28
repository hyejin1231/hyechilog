package com.myrou.hyechilog.api.controller.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
                        MockMvcRequestBuilders.post("/v1/api/createV1")
                                .contentType(MediaType.APPLICATION_JSON)
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
                        MockMvcRequestBuilders.post("/v1/api/createV2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("제목은 필수입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시글 등록을 요청할 때 title 값이 없으면 '제목은 필수입니다.' 에러 메시지 출력된다.")
    @Test
    void create() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("")
                .content("내용입니다.")
                .build();

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/api/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("제목은 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}