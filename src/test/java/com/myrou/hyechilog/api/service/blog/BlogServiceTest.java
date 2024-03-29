package com.myrou.hyechilog.api.service.blog;

import com.myrou.hyechilog.api.controller.blog.request.BlogCreateRequest;
import com.myrou.hyechilog.api.controller.blog.request.BlogEdit;
import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;
import com.myrou.hyechilog.api.domain.blog.User;
import com.myrou.hyechilog.api.exception.BlogNotFound;
import com.myrou.hyechilog.api.repository.blog.BlogRepository;
import com.myrou.hyechilog.api.repository.user.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        blogRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("게시글 저장한다.")
    void write() {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);

        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        // when
        BlogResponse blog = blogService.write(request, save.getId());

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

//    @Test
    @DisplayName("글을 10개 한번에 작성후, 저장하면 10개 다 저장된다.")
    void writes() {
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);

        List<BlogCreateRequest> blogCreateRequests = IntStream.range(1, 11)
                .mapToObj(i -> BlogCreateRequest.builder().title("제목 [" + i + "]").content("내용 [" + i + "]").build())
                .collect(Collectors.toList());

        List<BlogResponse> blogResponses = blogService.writes(blogCreateRequests, save.getId());

        assertThat(blogResponses).hasSize(10);
    }

    @Test
    @DisplayName("글을 저장하고 그 글의 id 값으로 조회한다.")
    void get() {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("Hello")
                .content("World!")
                .build();

        BlogResponse givenBlog = blogService.write(request, save.getId());

        // when
        BlogResponse whenBlog = blogService.get(givenBlog.getId());

        // then
        assertThat(whenBlog.getTitle()).isEqualTo(givenBlog.getTitle());
        assertThat(whenBlog.getContent()).isEqualTo(givenBlog.getContent());
    }
    
    
    @DisplayName("없는 게시글 조회했을 때는 '해당 글이 존재하지 않습니다.' 오류가 발생함.")
    @Test
    void getWhenNoBlog() {
        assertThatThrownBy(() -> blogService.get(2L)).isInstanceOf(
                BlogNotFound.class).hasMessage("해당 글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("글을 3개 저장하고 모든 글을 조회하면 3개의 글이 조회된다.")
    void getList() {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
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
        blogService.write(request1, save.getId());
        blogService.write(request2, save.getId());
        blogService.write(request3, save.getId());

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
    
    @Test
    @DisplayName("글 1개를 저장한 뒤 아이디를 가져와 해당 글을 수정하면 수정된 글을 반환한다.")
    void editBlog()
    {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        BlogCreateRequest request = BlogCreateRequest.builder()
                                                        .title("글 제목")
                                                        .content("글 내용").build();
        
        BlogResponse response = blogService.write(request, save.getId());
        
        BlogEdit blogEdit = BlogEdit.builder().title("글 제목 수정").content("글 내용").build();
        
        // when
        BlogResponse editResult = blogService.edit(response.getId(), blogEdit);
        
        // then
        assertThat(editResult.getTitle()).isEqualTo("글 제목 수정");
        assertThat(editResult.getContent()).isEqualTo("글 내용");
    }
    
    
    @DisplayName("게시글을 수정하려고 하는데 해당 블로그 id가 존재하지 않을 때 '해당 글이 존재하지 않습니다.' 오류가 발생함.")
    @Test
    void editWhenNoBlogId() {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("글 제목")
                .content("글 내용").build();
        
        BlogResponse response = blogService.write(request, save.getId());
        
        BlogEdit blogEdit = BlogEdit.builder().title("글 제목 수정").content("글 내용").build();
        
        // when then
        assertThatThrownBy(
                () -> blogService.edit(response.getId() + 1, blogEdit)).isInstanceOf(
                BlogNotFound.class).hasMessage("해당 글이 존재하지 않습니다.");
    }
    
    @Test
    @DisplayName("글 1개를 저장한 뒤 아이디를 가져와 해당 글의 내용만 수정하면 내용만 수정된다.")
    void editBlogWhenTitleIsNull()
    {
        // given
        User user = User.builder().name("hyechi").email("hyehilog@gmail.com").password("1231").build();
        User save = userRepository.save(user);
        BlogCreateRequest request = BlogCreateRequest.builder()
                .title("글 제목")
                .content("글 내용").build();
        
        BlogResponse response = blogService.write(request, save.getId());
        
        BlogEdit blogEdit = BlogEdit.builder().content("글 내용 수정~ ").build();
        
        // when
        BlogResponse editResult = blogService.edit(response.getId(), blogEdit);
        
        // then
        assertThat(editResult.getTitle()).isEqualTo("글 제목");
        assertThat(editResult.getContent()).isEqualTo("글 내용 수정~ ");
    }
    
    
    @DisplayName("게시글 1개를 작성해 저장한 다음 해당 글을 삭제하면 게시글 수가 0개이다.")
    @Test
    void delete() {
        // given
        Blog blog = Blog.builder().title("게시글 제목").content("게시글 내용").build();
        blogRepository.save(blog);
        
        // when
        blogService.delete(blog.getId());
        
        // then
        assertThat(blogRepository.count()).isZero();
    }
    
    
    @DisplayName("존재하지 않는 게시글을 삭제하려고 하면 해당 글이 존재하지 않습니다. 라는 오류 메시지가 출력된다.")
    @Test
    void deleteNoBlog() {
        assertThatThrownBy(() -> blogService.delete(2L))
                .isInstanceOf(BlogNotFound.class)
                .hasMessage("해당 글이 존재하지 않습니다.");
    }

}