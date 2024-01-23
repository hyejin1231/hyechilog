package com.myrou.hyechilog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/gets 요청 시 Hello World를 출력한다.")
    void getTest() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/gets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello world"))
                .andDo(MockMvcResultHandlers.print()); // 요청에 대한 요약 ? 로그르 확인할 수 있다.
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void postTest() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다.")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello Post World"))
                .andDo(MockMvcResultHandlers.print()); // 요청에 대한 요약 ? 로그르 확인할 수 있다.
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void postTest2() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/posts2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}") // JSON 형태
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello Post World"))
                .andDo(MockMvcResultHandlers.print()); // 요청에 대한 요약 ? 로그르 확인할 수 있다.
    }
}