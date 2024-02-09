package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

/**
 * 게시글이 존재하지 않을 때 사용하는 Custom Exception
 */
public class BlogNotFound extends BlogException
{
	private static final String MESSAGE = "해당 글이 존재하지 않습니다.";
	public BlogNotFound()
	{
		super(MESSAGE);
	}
	
	public BlogNotFound( Throwable cause)
	{
		super(MESSAGE, cause);
	}

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
