package com.myrou.hyechilog.api.controller.auth.request;

import com.myrou.hyechilog.api.domain.blog.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest
{
	private String email;
	private String password;
	
	private String name;
	
	@Builder
	public SignRequest(String email, String password, String name)
	{
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public static User toEntity(SignRequest signRequest)
	{
		return User.builder().email(signRequest.getEmail())
				.name(signRequest.getName()).password(signRequest.password)
				.build();
	}
}
