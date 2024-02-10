package com.myrou.hyechilog.api.docs.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public class BlogControllerDocsTest {

    private MockMvc mockMvc;

    @Autowired
    private BlogService blogService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        // 자동주입이 아니라 테스트 전에 미리 mockMvc 세팅하는 작업
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("블로그 글 작성 후 단건 조회하는 api docs 생성 테스트")
    void createRestDocsWithGetBlog() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder().title("Hyechii Blog Test").content("Blog Content").build();
        BlogResponse response = blogService.write(request);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/{blogId}", response.getId()).
                accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("index"));
    }

}
