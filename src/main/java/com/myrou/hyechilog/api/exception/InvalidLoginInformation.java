package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class InvalidLoginInformation extends BlogException
{
	private static final String MESSAGE = "아이디/비밀번호를 올바르게 입력하세요.";
	public InvalidLoginInformation()
	{
		super(MESSAGE);
	}
	
	@Override
	public HttpStatus getStatus()
	{
		return HttpStatus.BAD_REQUEST;
	}
}
