package com.myrou.hyechilog.api.controller.blog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BlogEdit
{
	@NotBlank( message = "title 값을 입력해주세요.")
	private String title;
	
	@NotBlank(message = "content 값을 입력해주세요.")
	private String content;
	
	@Builder
	public BlogEdit(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
}
