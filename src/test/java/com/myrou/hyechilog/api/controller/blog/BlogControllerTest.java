package com.myrou.hyechilog.api.controller.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.annotation.BlogWithMockUser;
import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.BlogEdit;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.api.service.blog.BlogService;
import com.myrou.hyechilog.api.service.blog.response.BlogResponse;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void setUp() {
        blogRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
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

    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
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

//    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
    @BlogWithMockUser
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

    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
    @DisplayName("게시글 등록할 때 제목에 '바보'가 포함되면 예외가 발생한다.")
    @Test
    void NotCreateWhenTitleContainsWord() throws Exception {
        // given
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("바보의 제목입니다!!")
                .content("내용입니다.!!")
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/blogs/new")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 작성 후 해당 id 값으로 조회하면 작성한 글을 조회한다.")
    void get() throws Exception {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);

        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("제목입니다!!")
                .content("내용입니다.!!")
                .build();

        BlogResponse givenBlog = blogService.write(request, save.getId());

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
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);

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
        BlogResponse response1 = blogService.write(request1, save.getId());
        BlogResponse response2 = blogService.write(request2, save.getId());
        BlogResponse response3 = blogService.write(request3, save.getId());


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/old/blogs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].id").value(response1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목입니다11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].content").value("내용입니다.11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].id").value(response2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].title").value("제목입니다22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].content").value("내용입니다.22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].id").value(response3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].title").value("제목입니다33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[2].content").value("내용입니다.33"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
    @DisplayName("게시글 30개 작성 후 첫번째 페이지 조회하면 10개의 글이 조회된다.")
    void getListWithPaging() throws Exception {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogService.writes(blogCreateRequests, save.getId());


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/old/paging/blogs?page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목 [30]"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[9].title").value("제목 [21]"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
    @DisplayName("게시글 30개 작성 후 첫번째 페이지 조회하면 10개의 글이 조회된다.")
    void getListWithQueryDsl() throws Exception {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 31)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 " + i).build())
                .collect(Collectors.toList());
        blogService.writes(blogCreateRequests, save.getId());


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs?page=0&size=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].title").value("제목 [30]"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[9].title").value("제목 [21]"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
    @BlogWithMockUser
    @DisplayName("글 1개를 저장하고 글 제목을 수정하면 수정한 블로그 글을 조회할 수 있다.")
    @Test
    void editBlog() throws Exception
    {
        // given
        User user = userRepository.findAll().get(0);

        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("글 제목")
                .content("글 내용").build();

        BlogResponse response = blogService.write(request, user.getId());

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


//    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
    @BlogWithMockUser
    @DisplayName("게시글 1개를 작성 후 삭제하면 해당 글이 삭제된다.")
    @Test
    void delete() throws Exception
    {
        // given
        User user = userRepository.findAll().get(0);
        BlogCreateRequest request = BlogCreateRequest.builder().title("게시글 제목")
                .content("게시글 내용").build();
        
        BlogResponse response = blogService.write(request, user.getId());
        
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

    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
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

//    @WithMockUser(username = "hyechilog@gmail.com", roles = {"ADMIN"}, password = "1231")
    @BlogWithMockUser
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