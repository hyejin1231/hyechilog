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
	
	public BlogEditor(String title, String content)
	{
		this.title = title;
		this.content = content;
	}
	
	public static BlogEditorBuilder builder() {
		return new BlogEditorBuilder();
	}
	
	public static class BlogEditorBuilder {
		private String title;
		private String content;
		
		BlogEditorBuilder() {
		}
		
		// 직접 Builder 클래스를 만들어서 넘어온 title이 null 이 아닐 경우에만 세팅하도록 해준다.
		public BlogEditorBuilder title(final String title) {
			if (title != null) {
				this.title = title;
			}
			return this;
		}
		
		// 넘어온 content 값이 null 이 아닌 경우에만 세팅해준다.
		public BlogEditorBuilder content(final String content) {
			if (content != null) {
				this.content = content;
			}
			return this;
		}
		
		public BlogEditor build() {
			return new BlogEditor(this.title, this.content);
		}
		
		public String toString() {
			return "BlogEditor.BlogEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
		}
	}
	
}
