package com.myrou.hyechilog.api.repository.blog;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.myrou.hyechilog.api.controller.blog.request.PageSearch;
import com.myrou.hyechilog.api.domain.blog.Blog;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogRepositoryImplTest
{
	
	@Autowired
	private BlogRepository blogRepository;
	
	
	@DisplayName("글 1페이지를 조회하면 10개의 글이 조회된다.")
	@Test
	void getList() {
	    // given
		List<Blog> blogs = IntStream.range(1, 31).mapToObj(
				i -> Blog.builder().title("제목 [" + i + "]")
						.content("내용 [" + i + "]").build()).toList();
		blogRepository.saveAll(blogs);
		
		PageSearch pageSearch = PageSearch.builder().page(1).size(10).build();
		
		// when
		List<Blog> resultBlogList = blogRepository.getList(pageSearch);
		
		// then
		assertThat(resultBlogList).hasSize(10);
		assertThat(resultBlogList.get(0).getTitle()).isEqualTo("제목 [30]");
		assertThat(resultBlogList.get(9).getTitle()).isEqualTo("제목 [21]");
	}
	
}