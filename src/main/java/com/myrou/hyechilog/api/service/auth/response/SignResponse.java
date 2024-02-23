package com.myrou.hyechilog.api.service.auth.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignResponse
{
	private String email;
	
	public SignResponse(String email)
	{
		this.email = email;
	}
}
