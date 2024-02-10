package com.myrou.hyechilog.api.docs.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs(
        uriScheme = "https", uriHost = "api.hyechilog.com", uriPort = 8080
)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class BlogControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        // 자동주입이 아니라 테스트 전에 미리 mockMvc 세팅하는 작업
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }

    @Test
    @DisplayName("블로그 글 작성 후 단건 조회하는 api docs 생성 테스트")
    void createRestDocsWithGetBlog() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder().title("Hyechii Blog Test").content("Blog Content").build();
        BlogResponse response = blogService.write(request);

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/blogs/{blogId}", response.getId()).
                        accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("index",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("blogId").description("게시글 Id")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),

                                PayloadDocumentation.fieldWithPath("data.id").description("게시글 Id"),
                                PayloadDocumentation.fieldWithPath("data.title").description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("data.content").description("게시글 내용")
                                )

                ));
    }

    @Test
    @DisplayName("게시글 작성 문서 테스트")
    void writeDocsTest() throws Exception {
        BlogCreateRequest request = BlogCreateRequest.builder().title("블로그 제목입니다.").content("블로그 내용입니다.").build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/blogs/new")
                .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("index",
                                PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("title").description("게시글 제목"),
                                        PayloadDocumentation.fieldWithPath("content").description("게시글 내용")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),

                                        PayloadDocumentation.fieldWithPath("data.id").description("게시글 Id"),
                                        PayloadDocumentation.fieldWithPath("data.title").description("게시글 제목"),
                                        PayloadDocumentation.fieldWithPath("data.content").description("게시글 내용")
                                )
                        )
                );
    }

}
