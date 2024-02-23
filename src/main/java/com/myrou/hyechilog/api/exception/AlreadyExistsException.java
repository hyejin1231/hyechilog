package com.myrou.hyechilog.api.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BlogException
{
	private static final String MESSAGE = "이미 가입된 이메일입니다.";
	
	public AlreadyExistsException()
	{
		super(MESSAGE);
	}
	
	@Override
	public HttpStatus getStatus()
	{
		return null;
	}
}
