package com.myrou.hyechilog.api.domain.blog;

import lombok.Builder;
import lombok.Getter;

/**
 * BlogE
 */
@Getter
public class BlogEditor
{
	private String title;
	private String content;
	
	@Builder
	public BlogEditor(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
}
