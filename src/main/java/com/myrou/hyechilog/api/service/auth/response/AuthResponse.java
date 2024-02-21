package com.myrou.hyechilog.api.service.auth.response;

import lombok.Getter;

@Getter
public class AuthResponse
{
	private String accessToken;
	
	public AuthResponse(String accessToken)
	{
		this.accessToken = accessToken;
	}
}
