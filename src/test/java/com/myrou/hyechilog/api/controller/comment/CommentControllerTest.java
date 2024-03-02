package com.myrou.hyechilog.api.controller.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrou.hyechilog.api.controller.comment.request.CommentCreateRequest;
import com.myrou.hyechilog.api.controller.comment.request.CommentDeleteRequest;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.domain.comment.Comment;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.repository.comment.CommentRepository;
import com.myrou.hyechilog.api.repository.user.UserRepository;
import com.myrou.hyechilog.support.crypto.PasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Test
    @DisplayName("댓글 작성 성공 테스트")
    void writeComments() throws Exception {
        // given
        User user = User.builder()
                .email("hyechilog@gmail.com")
                .password("1231")
                .name("hyechi")
                .build();
        User savedUser = userRepository.save(user);

        Blog blog = Blog.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(savedUser)
                .build();
        Blog savedBlog = blogRepository.save(blog);

        CommentCreateRequest createRequest = CommentCreateRequest.builder()
                .author("testUser1")
                .password("12345678")
                .content("댓글 내용 테스트 입니다. Hi ~~~~~~~~~~~~~~~~~~~~~~")
                .build();

        // when.. then
        mockMvc.perform(MockMvcRequestBuilders.post("/blog/{blogId}/comments", savedBlog.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Comment savedComment = commentRepository.findAll().get(0);
        assertThat(savedComment.getAuthor()).isEqualTo("testUser1");
        assertThat(savedComment.getContent()).isEqualTo("댓글 내용 테스트 입니다. Hi ~~~~~~~~~~~~~~~~~~~~~~");
        assertThat(savedComment.getPassword()).isNotEqualTo("12345678");
        assertTrue(passwordEncoder.matches("12345678", savedComment.getPassword()));

    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteComment() throws Exception {
        User user = User.builder()
                .email("hyechilog@gmail.com")
                .password("1231")
                .name("hyechi")
                .build();
        User savedUser = userRepository.save(user);

        Blog blog = Blog.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .user(savedUser)
                .build();
        Blog savedBlog = blogRepository.save(blog);

        String encrypt = passwordEncoder.encrypt("123456");
        Comment comment = Comment.builder()
                .author("testUser")
                .content("댓글 내용입니다. ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ")
                .password(encrypt)
                .build();
        comment.setBlog(savedBlog);
        Comment savedComment = commentRepository.save(comment);

        CommentDeleteRequest commentDeleteRequest = new CommentDeleteRequest("123456");

        // when.. then
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/{commentId}/delete", savedComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDeleteRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}